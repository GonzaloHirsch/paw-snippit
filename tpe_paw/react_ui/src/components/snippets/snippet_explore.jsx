import React, { Component } from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";

// Stateless Functional Component

const SnippetExplore = (props) => {
  // FIXME -> snippet count
  return (
    <React.Fragment>
      <div className="mx-3 mb-3 fw-100">
        <h1 className="fw-300">{i18n.t("nav.explore")}</h1>
        <h5 className="fw-100">
          ({props.totalSnippets} {i18n.t("snippets")})
        </h5>
      </div>
      <div>
        <form></form>
        <SnippetFeed
          snippets={props.snippets}
          links={props.links}
          currentPage={props.currentPage}
          onPageTransition={props.onPageTransition}
          onSnippetFav={props.onSnippetFav}
          userIsLogged={props.userIsLogged}
        />
      </div>
    </React.Fragment>
  );
};

export default SnippetExplore;
