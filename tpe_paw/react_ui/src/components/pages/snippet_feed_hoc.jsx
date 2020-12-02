import React from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import { extractLinkHeaders, extractItemCountHeader } from "../../js/api_utils";
import store from "../../store";
import { withRouter, matchPath } from "react-router-dom";
import shallowCompare from "react-addons-shallow-compare";
import { areEqualShallow } from "../../js/comparison";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

function SnippetFeedHOC(WrappedComponent, getSnippets, searchSnippets) {
  return withRouter(
    class extends React.Component {
      snippetFeedClient;

      constructor(props) {
        super(props);
        const state = store.getState();
        if (state.auth.token === null || state.auth.token === undefined) {
          this.snippetFeedClient = new SnippetFeedClient();
        } else {
          this.snippetFeedClient = new SnippetFeedClient(state.auth.token);
        }

        this.onPageTransition = this.onPageTransition.bind(this);

        // Keeping track of the search
        const search = this.getSearchFromUrl();

        // Initial state
        this.state = {
          snippets: [],
          currentPage: 1,
          totalSnippets: 0,
          links: {},
          currentSearch: search,
        };
      }

      // Loading data

      loadSnippets(page) {
        getSnippets(this.snippetFeedClient, page)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);
            this.setState({
              links: newLinks,
              snippets: res.data,
              totalSnippets: itemCount,
            });
          })
          .catch((e) => {});
      }

      loadSearchedSnippets(page, search) {
        searchSnippets(this.snippetFeedClient, page, search)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);
            this.setState(
              {
                links: newLinks,
                snippets: res.data,
                totalSnippets: itemCount,
              }
            );
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

      getSearchFromUrl() {
        const params = new URLSearchParams(this.props.location.search);
        let search = {};
        search.query = params.get("query");
        search.type = params.get("type");
        search.sort = params.get("sort");
        return search;
      }

      // Lifecycle hooks

      componentDidMount() {
        const isSearching = !!matchPath(
          this.props.location.pathname,
          "**/search"
        );
        if (isSearching) {
          const search = this.getSearchFromUrl();
          this.loadSearchedSnippets(this.state.currentPage, search);
        } else {
          this.loadSnippets(this.state.currentPage);
        }
      }

      componentDidUpdate() {
        this.checkIfLoadSnippets();
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

      checkIfLoadSnippets() {
        let pageParam = this.getPageFromUrl();
        const isSearching = !!matchPath(
          this.props.location.pathname,
          "**/search"
        );

        let reload = false;
        let search = {};

        if (isSearching) {
          search = this.getSearchFromUrl();
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
              {...this.state}
            ></WrappedComponent>
          </div>
        );
      }
    }
  );
}

export default SnippetFeedHOC;
