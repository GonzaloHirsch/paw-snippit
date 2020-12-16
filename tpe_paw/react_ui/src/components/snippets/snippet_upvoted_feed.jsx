import React from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";
import { Helmet } from "react-helmet";

// Stateless Functional Component

const SnippetUpvotedFeed = (props) => {
  return (
    <React.Fragment>
      <Helmet>
        <title>
          {i18n.t("app")} | {i18n.t("nav.upvoted")}
        </title>
      </Helmet>
      <div className="mx-3 mb-3">
        <h1 className="fw-300">{i18n.t("nav.upvoted")}</h1>
        <h5 className="fw-100">
          ({i18n.t("snippetWithNumber", { count: props.totalSnippets })})
        </h5>
      </div>
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
    </React.Fragment>
  );
};

export default SnippetUpvotedFeed;
