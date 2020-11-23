import React, { Component } from "react";

class SnippetDetail extends Component {
  state = {};
  render({ match }) {
    const snippetId = match.params.id;
    return <h1>{"MY SNIPPET IS " + snippetId}</h1>;
  }
}

export default SnippetDetail;
