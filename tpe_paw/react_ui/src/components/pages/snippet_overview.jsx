import React, { Component } from "react";
import SnippetDetail from "../snippets/snippet_detail";
import SnippetClient from "../../api/implementations/SnippetOverviewClient";
import UserClient from "../../api/implementations/UserClient";
import LanguageClient from "../../api/implementations/LanguageClient";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

class SnippetOverview extends Component {
  snippetClient;
  userClient;
  languageClient;

  constructor(props) {
    super(props);
    this.snippetClient = new SnippetClient();
    this.userClient = new UserClient();
    this.languageClient = new LanguageClient();
    this.state = {
      snippet: "",
      creator: "",
      language: "",
    };
  }

  loadSnippet() {
    const snippetId = this.props.match.params.id;

    this.snippetClient
      .getSnippetWithId(snippetId)
      .then((res) => {
        this.setState({ snippet: res.data });
        console.log(this.state.snippet);

        // If snippet was found, obtain the creator
        this.userClient
          .getUserWithUrl(this.state.snippet.creator)
          .then((res) => {
            this.setState({ creator: res.data });
          })
          .catch((e) => {});

        // If snippet was found, obtain the language
        this.languageClient
          .getLanguageWithUrl(this.state.snippet.language)
          .then((res) => {
            this.setState({ language: res.data });
          })
          .catch((e) => {});
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this.loadSnippet();
  }

  render() {
    return (
      <div className="flex-center">
        <SnippetDetail {...this.state} />
      </div>
    );
  }
}

export default SnippetOverview;
