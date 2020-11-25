import React, { Component } from "react";
import SnippetCard from "./snippet_card";
import Pagination from "../navigation/pagination";

class SnippetFeed extends Component {
  state = {};

  render() {
    const { snippets, page, links } = this.props;
    return (
      <React.Fragment>
        <div className="card-columns mx-3">
          {snippets.map((snippet) => (
            <SnippetCard key={snippet.id} snippet={snippet} />
          ))}
        </div>
        <div style={{display: "flex", justifyContent: "center"}}>
          <Pagination
            currentPage={page}
            links={links}
            onPageTransition={this.props.onPageTransition}
            className="mx-auto"
          />
        </div>
      </React.Fragment>
    );
  }
}

export default SnippetFeed;
