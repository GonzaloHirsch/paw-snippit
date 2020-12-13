import React from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import { extractLinkHeaders, extractItemCountHeader } from "../../js/api_utils";
import { getSnippetPositionInArray } from "../../js/snippet_utils";
import store from "../../store";
import { withRouter, matchPath } from "react-router-dom";
import { areEqualShallow } from "../../js/comparison";
import SnippetActionsClient from "../../api/implementations/SnippetActionsClient";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

function SnippetFeedHOC(
  WrappedComponent,
  getSnippets,
  searchSnippets,
  searchFromUrl,
  tokenProtected
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
          this.snippetActionsClient = new SnippetActionsClient();
          this.snippetFeedClient = new SnippetFeedClient();
        } else {
          if (!tokenProtected) {
            this.snippetFeedClient = new SnippetFeedClient();
          } else {
            this.snippetFeedClient = new SnippetFeedClient(
              props,
              state.auth.token
            );
          }
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
        };
      }

      // Loading data

      loadSnippets(page) {
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
              });
            }
          })
          .catch((e) => {});
      }

      loadSearchedSnippets(page, search) {
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
              });
            }
          })
          .catch((e) => {});
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
            .catch((e) => {});
        }
        // Was not fav, not it is
        else {
          this.snippetActionsClient
            .favSnippet(id)
            .then((res) => {})
            .catch((e) => {});
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

      render() {
        return (
          <div>
            <WrappedComponent
              onPageTransition={this.onPageTransition}
              onSnippetFav={this.onSnippetFav}
              {...this.state}
              {...this.props}
            ></WrappedComponent>
          </div>
        );
      }
    }
  );
}

export default SnippetFeedHOC;
