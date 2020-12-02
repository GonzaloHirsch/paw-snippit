import React, { Component } from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";

// Stateless Functional Component

const SnippetUpvotedFeed = (props) => {
  return (
    <React.Fragment>
      <div className="mx-3 mb-3">
        <h1 className="fw-300">{i18n.t("nav.upvoted")}</h1>
        <h5 className="fw-100">({props.totalSnippets} {i18n.t("snippets")})</h5>
      </div>
      <SnippetFeed
        snippets={props.snippets}
        links={props.links}
        currentPage={props.currentPage}
        onPageTransition={props.onPageTransition}
      />
    </React.Fragment>
  );
};

export default SnippetUpvotedFeed;
