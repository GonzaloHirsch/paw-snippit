import React, { Component } from "react";
import SnippetCard from "./snippet_card";

class SnippetFeed extends Component {
  state = {};
  render() {
    return (
      <div class="card-columns mx-3">
        {this.props.snippets.map((snippet) => (
          <SnippetCard snippet={snippet} />
        ))}
      </div>
    );
  }
}

export default SnippetFeed;
