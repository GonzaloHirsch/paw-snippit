import React, { Component } from "react";
import SnippetFeed from "../snippets/snippet_feed";
import SnippetsClient from "../../api/implementations/SnippetsClient";
import extractLinkHeaders from "../../js/api_utils";

class Home extends Component {
  snippetsClient;

  state = {
    snippets: [],
    currentPage: 1,
    links: {},
  };

  constructor(props) {
    super(props);
    this.snippetsClient = new SnippetsClient();
  }

  loadSnippets() {
    this.snippetsClient
      .getSnippetFeed(this.state.page)
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
    this.snippetsClient
      .getSnippetFeedWithUrl(this.state.links[moveTo].url)
      .then((res) => {
        // Extracting the other pages headers
        const newLinks = extractLinkHeaders(res.headers["link"]);
        this.setState({ links: newLinks, snippets: res.data, currentPage: this.state.links[moveTo].page });
      })
      .catch((e) => {});
  };

  render() {
    return (
      <React.Fragment>
        <h1>HOME</h1>
        <SnippetFeed
          snippets={this.state.snippets}
          links={this.state.links}
          page={this.state.currentPage}
          onPageTransition={this.onPageTransition}
        />
      </React.Fragment>
    );
  }
}

export default Home;
