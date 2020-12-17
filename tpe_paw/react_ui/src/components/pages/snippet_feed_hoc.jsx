import React from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import { extractLinkHeaders, extractItemCountHeader } from "../../js/api_utils";
import { getSnippetPositionInArray } from "../../js/snippet_utils";
import store from "../../store";
import { withRouter, matchPath } from "react-router-dom";
import { areEqualShallow } from "../../js/comparison";
import SnippetActionsClient from "../../api/implementations/SnippetActionsClient";
import { Alert } from "reactstrap";
import i18n from "../../i18n";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

function SnippetFeedHOC(
  WrappedComponent,
  getSnippets,
  searchSnippets,
  searchFromUrl
) {
  return withRouter(
    class extends React.Component {
      // Variable to avoid loading on unmounted component
      _isMounted = false;
      snippetFeedClient;
      snippetActionsClient;

      constructor(props) {
        super(props);
        const state = store.getState();
        let userIsLogged = false;
        if (state.auth.token === null || state.auth.token === undefined) {
          this.snippetActionsClient = new SnippetActionsClient(this.props);
          this.snippetFeedClient = new SnippetFeedClient(this.props);
        } else {
          this.snippetFeedClient = new SnippetFeedClient(
            props,
            state.auth.token
          );
          this.snippetActionsClient = new SnippetActionsClient(
            props,
            state.auth.token
          );
          userIsLogged = true;
        }

        this.onPageTransition = this.onPageTransition.bind(this);
        this.onSnippetFav = this.onSnippetFav.bind(this);

        // Keeping track of the search
        const search = searchFromUrl(this.props.location.search);

        // Initial state
        this.state = {
          snippets: [],
          currentPage: 1,
          totalSnippets: 0,
          links: {},
          currentSearch: search,
          userIsLogged: userIsLogged,
          loading: false,
          alert: {
            show: false,
            message: "",
          },
        };
      }

      // Loading data

      loadSnippets(page) {
        this.setState({ loading: true });
        getSnippets(this.snippetFeedClient, page)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);
            if (this._isMounted) {
              this.setState({
                links: newLinks,
                snippets: res.data,
                totalSnippets: itemCount,
                loading: false,
              });
            }
          })
          .catch((e) => {
            if (e.response) {
              // Error is not 400, 401, 403 or 500
              const alert = {
                show: true,
                message: i18n.t("errors.unknownError"),
              };
              this.setState({ alert: alert });
            }
            this.setState({ loading: false });
          });
      }

      loadSearchedSnippets(page, search) {
        this.setState({ loading: true });
        searchSnippets(this.snippetFeedClient, page, search)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);
            if (this._isMounted) {
              this.setState({
                links: newLinks,
                snippets: res.data,
                totalSnippets: itemCount,
                loading: false,
              });
            }
          })
          .catch((e) => {
            if (e.response) {
              // client received an error response (5xx, 4xx)
              if (e.response.status === 400) {
                // BAD REQUEST -> Show alert
                const alert = { show: true, message: i18n.t("errors.e400") };
                this.setState({ alert: alert });
              } else {
                // Error is not 400, 401, 403 or 500
                const alert = {
                  show: true,
                  message: i18n.t("errors.unknownError"),
                };
                this.setState({ alert: alert });
              }
            }
            // Stop loading in case of error
            this.setState({ loading: false });
          });
      }

      // Recover data from the URL

      getPageFromUrl() {
        const params = new URLSearchParams(this.props.location.search);
        let pageParam = params.get("page");
        if (pageParam === null || pageParam === undefined || pageParam === 0) {
          return 1;
        }
        return parseInt(pageParam, 10);
      }

      // Lifecycle hooks

      componentDidMount() {
        this._isMounted = true;

        const isSearching = !!matchPath(
          this.props.location.pathname,
          "**/search"
        );
        if (isSearching) {
          const search = searchFromUrl(this.props.location.search);
          this.loadSearchedSnippets(this.state.currentPage, search);
        } else {
          this.loadSnippets(this.state.currentPage);
        }
      }

      componentDidUpdate() {
        this.checkIfLoadSnippets();
      }

      componentWillUnmount() {
        this._isMounted = false;
      }

      // Events

      onPageTransition = (moveTo) => {
        const pageToMove = parseInt(
          new URLSearchParams(this.state.links[moveTo].url.split("?")[1]).get(
            "page"
          ),
          10
        );
        // Adding the params to not lose the existing ones
        let params = new URLSearchParams(this.props.location.search);
        params.set("page", pageToMove);

        // Pushing the route
        this.props.history.push({
          pathname: this.props.location.pathname,
          search: "?" + params.toString(),
        });
      };

      onSnippetFav = (e, id) => {
        let previousFavState = false;
        // Impact the local snippet state
        // Copy snippets array
        let snippets = [...this.state.snippets];
        // Find index of our item
        const i = getSnippetPositionInArray(snippets, id);
        // Copy item
        let snippet = { ...snippets[i] };
        // Store previous fav state
        previousFavState = snippet.favorite;
        // Update variable
        snippet.favorite = !snippet.favorite;
        // Place item again
        snippets[i] = snippet;
        // Update state
        this.setState({ snippets: snippets });

        // Was faved, now not
        if (previousFavState) {
          this.snippetActionsClient
            .unfavSnippet(id)
            .then((res) => {})
            .catch((e) => {
              if (e.response) {
                // Error is not 400, 401, 403 or 500
                const alert = {
                  show: true,
                  message: i18n.t("errors.unknownError"),
                };
                this.setState({ alert: alert });
              }
            });
        }
        // Was not fav, not it is
        else {
          this.snippetActionsClient
            .favSnippet(id)
            .then((res) => {})
            .catch((e) => {
              if (e.response) {
                // Error is not 400, 401, 403 or 500
                const alert = {
                  show: true,
                  message: i18n.t("errors.unknownError"),
                };
                this.setState({ alert: alert });
              }
            });
        }

        // Prevent parent Link to navigate
        e.preventDefault();
      };

      checkIfLoadSnippets() {
        let pageParam = this.getPageFromUrl();
        const isSearching = !!matchPath(
          this.props.location.pathname,
          "**/search"
        );

        let reload = false;
        let search = {};

        if (isSearching) {
          search = searchFromUrl(this.props.location.search);
          if (!areEqualShallow(search, this.state.currentSearch)) {
            reload = true;
          }
        }
        if (pageParam !== this.state.currentPage) {
          reload = true;
        }

        if (reload) {
          if (isSearching) {
            this.setState(
              { currentPage: pageParam, currentSearch: search },
              () => {
                // Retrieving the page param
                this.loadSearchedSnippets(pageParam, search);
              }
            );
          } else {
            this.setState({ currentPage: pageParam }, () => {
              // Retrieving the page param
              this.loadSnippets(pageParam);
            });
          }
        }
      }

      // To dismiss the alert in case of error
      onDismiss = () => {
        const alert = { show: false, message: "" };
        this.setState({ alert: alert });
      };

      render() {
        return (
          <div>
            <WrappedComponent
              onPageTransition={this.onPageTransition}
              onSnippetFav={this.onSnippetFav}
              {...this.state}
              {...this.props}
            ></WrappedComponent>
            <Alert
              color="danger"
              className="shadow flex-center custom-alert"
              isOpen={this.state.alert.show}
              toggle={() => this.onDismiss()}
            >
              {this.state.alert.message}
            </Alert>
          </div>
        );
      }
    }
  );
}

export default SnippetFeedHOC;
