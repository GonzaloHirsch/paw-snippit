import React, { Component } from "react";
import SnippetFeed from "../snippets/snippet_feed";
import i18n from "../../i18n";
import ExploreForm from "./explore_form";

// Stateless Functional Component

const SnippetExplore = (props) => {
  return (
    <React.Fragment>
      <div className="mx-3 mb-3 fw-100">
        <h1 className="fw-300">{i18n.t("nav.explore")}</h1>
        <h5 className="fw-100">
          ({props.totalSnippets} {i18n.t("snippets")})
        </h5>
      </div>
      <div className="row">
        <div className="col-2 explore-margin p-0">
          <div className="p-3 rounded-border explore-border">
            <ExploreForm />
          </div>
        </div>

        <div className="col-9">
          <SnippetFeed
            snippets={props.snippets}
            links={props.links}
            currentPage={props.currentPage}
            onPageTransition={props.onPageTransition}
          />
        </div>
      </div>
    </React.Fragment>
  );
};

export default SnippetExplore;
