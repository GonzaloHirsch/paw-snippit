import React from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";
import { Link } from "react-router-dom";

const SnippetProfileFeed = (props) => {
  return (
    <React.Fragment>
      <SnippetFeed
        snippets={props.snippets}
        totalSnippets={props.totalSnippets}
        links={props.links}
        currentPage={props.currentPage}
        onPageTransition={props.onPageTransition}
        onPageTransitionWithPage={props.onPageTransitionWithPage}
        onSnippetFav={props.onSnippetFav}
        userIsLogged={props.userIsLogged}
        loading={props.loading}
      />
      {!props.isSearching &&
      !props.loading &&
      props.isOwner &&
      props.totalSnippets === 0 ? (
        <div className="flex-center flex-col my-3">
          <h2 className="fw-100">{i18n.t("snippetCreate.message")}</h2>
          <Link
            to="/snippets/create"
            className="no-margin shadow btn btn-lg btn-block mt-2 rounded-border form-button text-white create-button-profile"
          >
            {i18n.t("snippetCreate.header")}
          </Link>
        </div>
      ) : null}
    </React.Fragment>
  );
};

export default SnippetProfileFeed;
