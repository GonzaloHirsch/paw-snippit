import React from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";
import ExploreForm from "../forms/explore_form";
import { Helmet } from "react-helmet";

// Stateless Functional Component

const SnippetExplore = (props) => {
  return (
    <React.Fragment>
      <Helmet>
        <title>
          {i18n.t("app")} | {i18n.t("nav.explore")}
        </title>
      </Helmet>
      <div className="mx-3 mb-3 fw-100">
        <h1 className="fw-300">{i18n.t("nav.explore")}</h1>
        <h5 className="fw-100">
          ({props.totalSnippets} {i18n.t("snippets")})
        </h5>
      </div>
      <div className="row">
        <div className="col-3 py-0 pb-4 pr-0 no-margin explore-padding">
          <div className="p-3 rounded-border explore-border">
            <ExploreForm urlSearch={props.currentSearch} />
          </div>
        </div>

        <div className="col-9">
          <SnippetFeed
            snippets={props.snippets}
            totalSnippets={props.totalSnippets}
            links={props.links}
            currentPage={props.currentPage}
            onPageTransition={props.onPageTransition}
            onSnippetFav={props.onSnippetFav}
            userIsLogged={props.userIsLogged}
            loading={props.loading}
          />
        </div>
      </div>
    </React.Fragment>
  );
};

export default SnippetExplore;
