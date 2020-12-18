import React from "react";
import LanguagesAndTagsActionsClient from "../../api/implementations/LanguagesAndTagsActionsClient";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import { extractLinkHeaders, extractItemCountHeader } from "../../js/api_utils";
import { getItemPositionInArray } from "../../js/item_utils";
import store from "../../store";
import { withRouter, matchPath } from "react-router-dom";
import { areEqualShallow } from "../../js/comparison";
import { Alert } from "reactstrap";
import i18n from "../../i18n";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

function ItemFeedHOC(
  WrappedComponent,
  getItems,
  searchItems,
  searchFromUrl,
  tokenProtected
) {
  return withRouter(
    class extends React.Component {
      // Variable to avoid loading on unmounted component
      _isMounted = false;
      latClient;
      latActionsClient;

      constructor(props) {
        super(props);
        const state = store.getState();
        let userIsLogged = false;
        if (state.auth.token === null || state.auth.token === undefined) {
          this.latActionsClient = new LanguagesAndTagsActionsClient(this.props);
          this.latClient = new LanguagesAndTagsClient(this.props);
        } else {
          if (!tokenProtected) {
            this.latClient = new LanguagesAndTagsClient(this.props);
          } else {
            this.latClient = new LanguagesAndTagsClient(
              props,
              state.auth.token
            );
          }
          this.latActionsClient = new LanguagesAndTagsActionsClient(
            props,
            state.auth.token
          );
          userIsLogged = true;
        }

        this.onPageTransition = this.onPageTransition.bind(this);
        this.onPageTransitionWithPage = this.onPageTransitionWithPage.bind(this);
        this.handleChangeFollowing = this.handleChangeFollowing.bind(this);

        // Keeping track of the search
        const search = searchFromUrl(this.props.location.search);

        // Initial state
        this.state = {
          items: [],
          currentPage: 1,
          totalItems: 0,
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

      loadItems(page) {
        this.setState({ loading: true });
        getItems(this.latClient, page)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);
            if (this._isMounted) {
              this.setState({
                links: newLinks,
                items: res.data,
                totalItems: itemCount,
                loading: false,
              });
            }
          })
          .catch((e) => {
            this._handleError(e);
          });
      }

      loadSearchedItems(page, search) {
        this.setState({ loading: true });
        searchItems(this.latClient, page, search)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);
            if (this._isMounted) {
              this.setState({
                links: newLinks,
                items: res.data,
                totalItems: itemCount,
                loading: false,
              });
            }
          })
          .catch((e) => {
            this._handleError(e);
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
          this.loadSearchedItems(this.state.currentPage, search);
        } else {
          this.loadItems(this.state.currentPage);
        }
      }

      componentDidUpdate() {
        this.checkIfLoadItems();
      }

      componentWillUnmount() {
        this._isMounted = false;
      }

      // Events

      _handleError(e) {
        if (e.response) {
          // Error is not 401, 403, 404 or 500
          const alert = {
            show: true,
            message: i18n.t("errors.unknownError"),
          };
          this.setState({ alert: alert });
        }
        this.setState({ loading: false });
      }

      handleChangeFollowing(e, id) {
        let previousFollowState = false;
        // Impact the local snippet state
        // Copy snippets array
        let items = [...this.state.items];
        // Find index of our item
        const i = getItemPositionInArray(items, id);
        // Copy item
        let item = { ...items[i] };
        // Store previous fav state
        previousFollowState = item.following;
        // Update variable
        item.following = !item.following;
        // Place item again
        items[i] = item;
        // Update state
        this.setState({ items: items });

        // Was faved, now not
        if (previousFollowState) {
          this.latActionsClient
            .unfollowTag(id)
            .then((res) => {})
            .catch((e) => {
              this._handleError(e);
            });
        }
        // Was not fav, not it is
        else {
          this.latActionsClient
            .followTag(id)
            .then((res) => {})
            .catch((e) => {
              this._handleError(e);
            });
        }

        // Prevent parent Link to navigate
        e.preventDefault();
      }

      onPageTransition = (moveTo) => {
        const pageToMove = parseInt(
          new URLSearchParams(this.state.links[moveTo].url.split("?")[1]).get(
            "page"
          ),
          10
        );
        this.onPageTransitionWithPage(pageToMove);
      };

      onPageTransitionWithPage = (page) => {
        // Adding the params to not lose the existing ones
        let params = new URLSearchParams(this.props.location.search);
        params.set("page", page);

        // Pushing the route
        this.props.history.push({
          pathname: this.props.location.pathname,
          search: "?" + params.toString(),
        });
      }

      checkIfLoadItems() {
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
                this.loadSearchedItems(pageParam, search);
              }
            );
          } else {
            this.setState({ currentPage: pageParam }, () => {
              // Retrieving the page param
              this.loadItems(pageParam);
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
              onPageTransitionWithPage={this.onPageTransitionWithPage}
              handleChangeFollowing={this.handleChangeFollowing}
              {...this.state}
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

export default ItemFeedHOC;
