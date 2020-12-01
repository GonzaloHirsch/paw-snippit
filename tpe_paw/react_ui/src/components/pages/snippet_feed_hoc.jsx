import React from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import { extractLinkHeaders, extractItemCountHeader } from "../../js/api_utils";
import store from "../../store";
import { withRouter } from "react-router-dom";

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
        this.state = {
          snippets: [],
          currentPage: 1,
          totalSnippets: 0,
          links: {},
        };
      }

      loadSnippets(page) {
        console.log(page, "PGEEEE");
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

      componentDidMount() {
        const params = new URLSearchParams(this.props.location.search);
        let pageParam = parseInt(params.get("page"), 10);
        if (
          pageParam !== null &&
          pageParam !== undefined &&
          pageParam > 0 &&
          pageParam !== this.state.currentPage
        ) {
          this.setState({ currentPage: pageParam });
        } else {
          pageParam = this.state.currentPage;
        }
        // Retrieving the page param
        this.loadSnippets(pageParam);
      }

      onPageTransition = (moveTo) => {
        console.log("TRANS")
        const pageToMove = parseInt(new URLSearchParams(
          this.state.links[moveTo].url.split("?")[1]
        ).get("page"), 10);
        // Adding the params to not lose the existing ones
        let params = new URLSearchParams(this.props.location.search);
        params.set("page", pageToMove);

        // Pushing the route
        this.props.history.push({
          pathname: this.props.location.pathname,
          search: "?" + params.toString(),
        });

        // Fetching the new snippets
        this.snippetFeedClient
          .getSnippetFeedWithUrl(this.state.links[moveTo].url)
          .then((res) => {
            // Extracting the other pages headers
            const newLinks = extractLinkHeaders(res.headers);
            const itemCount = extractItemCountHeader(res.headers);

            this.setState({
              links: newLinks,
              snippets: res.data,
              currentPage: this.state.links[moveTo].page,
              totalSnippets: itemCount,
            });
          })
          .catch((e) => {});
      };

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
