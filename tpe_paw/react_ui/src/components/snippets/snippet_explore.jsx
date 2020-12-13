import React, { Component } from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";
import ExploreForm from "../forms/explore_form";

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
        <div className="col-3 explore-margin p-0">
          <div className="p-3 rounded-border explore-border">
            <ExploreForm urlSearch={props.currentSearch} />
          </div>
        </div>

        <div className="col-8">
          <SnippetFeed
            snippets={props.snippets}
            totalSnippets={props.totalSnippets}
            links={props.links}
            currentPage={props.currentPage}
            onPageTransition={props.onPageTransition}
            onSnippetFav={props.onSnippetFav}
            userIsLogged={props.userIsLogged}
          />
        </div>
      </div>
    </React.Fragment>
  );
};

export default SnippetExplore;
