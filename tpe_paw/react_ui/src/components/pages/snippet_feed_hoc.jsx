import React, { Component } from "react";
import SnippetFeed from "../snippets/snippet_feed";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import extractLinkHeaders from "../../js/api_utils";

function SnippetFeedHOC(WrappedComponent, context, getSnippets) {
  return class extends React.Component {
    snippetFeedClient;

    constructor(props) {
      super(props);
      this.snippetFeedClient = new SnippetFeedClient();
      this.onPageTransition = this.onPageTransition.bind(this);
      this.state = {
        snippets: [],
        currentPage: 1,
        links: {},
      };
    }

    loadSnippets() {
      getSnippets(this.snippetFeedClient, this.state.page)
        .then((res) => {
          // Extracting the other pages headers
          const newLinks = extractLinkHeaders(res.headers["link"]);
          this.setState({ links: newLinks, snippets: res.data });
          console.log(res.data);
          console.log(this.state.snippets);
          console.log(this.state.links);
        })
        .catch((e) => {});
    }

    componentDidMount() {
      this.loadSnippets();
    }

    onPageTransition = (moveTo) => {
      this.snippetFeedClient
        .getSnippetFeedWithUrl(this.state.links[moveTo].url)
        .then((res) => {
          // Extracting the other pages headers
          const newLinks = extractLinkHeaders(res.headers["link"]);
          this.setState({
            links: newLinks,
            snippets: res.data,
            currentPage: this.state.links[moveTo].page,
          });
        })
        .catch((e) => {});
    };

    render() {
      return (
        <div>
          <WrappedComponent
            context={context}
            onPageTransition={this.onPageTransition}
            {...this.state}
          ></WrappedComponent>
        </div>
      );
    }
  };
}

export default SnippetFeedHOC;
