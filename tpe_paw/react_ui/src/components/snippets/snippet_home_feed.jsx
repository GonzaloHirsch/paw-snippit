import React, { Component } from "react";
import SnippetFeed from "./snippet_feed";

// Stateless Functional Component

const SnippetHomeFeed = (props) => {
  // FIXME -> snippet count
  return (
    <React.Fragment>
      <div className="mx-3 mb-3">
        <h1>Home</h1>
        <h5>({props.snippets.length} snippets)</h5>
      </div>
      <SnippetFeed
        snippets={props.snippets}
        links={props.links}
        page={props.currentPage}
        onPageTransition={props.onPageTransition}
      />
    </React.Fragment>
  );
};

export default SnippetHomeFeed;
