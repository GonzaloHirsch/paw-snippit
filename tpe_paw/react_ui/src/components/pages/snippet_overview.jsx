import React, { Component } from "react";
import SnippetDetail from "../snippets/snippet_detail";
import SnippetClient from "../../api/implementations/SnippetClient";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

class SnippetOverview extends Component {
  snippetClient;

  constructor(props) {
    super(props);
    this.snippetClient = new SnippetClient();
    this.state = {
      snippet: "",
    };
  }

  loadSnippet() {
    const snippetId = this.props.match.params.id;

    this.snippetClient
      .getSnippetWithId(snippetId)
      .then((res) => {
        this.setState({ snippet: res.data });
        console.log(this.state.snippet);
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this.loadSnippet();
  }

  render() {
    return <SnippetDetail {...this.state} />;
  }
}

export default SnippetOverview;
