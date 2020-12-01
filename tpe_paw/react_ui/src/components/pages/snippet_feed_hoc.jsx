import React from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import { extractLinkHeaders, extractItemCountHeader } from "../../js/api_utils";
import store from "../../store";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

function SnippetFeedHOC(WrappedComponent, getSnippets) {
  return class extends React.Component {
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

    loadSnippets() {
      getSnippets(this.snippetFeedClient, this.state.page)
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
      this.loadSnippets();
    }

    componentDidUpdate(oldProps){
      console.log("OLD", oldProps)
      console.log("NEW", this.props)
    }

    onPageTransition = (moveTo) => {
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
  };
}

export default SnippetFeedHOC;
