import React from "react";
import SnippetCard from "./snippet_card";
import Pagination from "../navigation/pagination";
import i18n from "../../i18n";
import { Spinner } from "reactstrap";

// Stateless Functional Component

const SnippetFeed = (props) => {
  const {
    snippets,
    totalSnippets,
    currentPage,
    links,
    onPageTransition,
    onPageTransitionWithPage,
    onSnippetFav,
    userIsLogged,
    loading,
  } = props;
  return (
    <div className="parent-width flex-column">
      {loading && (
        <div className="align-items-vertical align-items-horizontal-center">
          <Spinner color="dark" style={{ width: "3rem", height: "3rem" }} />
          </div>
      )}
      {!loading &&
        (totalSnippets === 0 ? (
          <div
            className="flex-center fw-100 text-center"
            style={{ fontSize: "50px" }}
          >
            {i18n.t("snippetsEmpty")}
          </div>
        ) : (
          <React.Fragment>
            <div className="card-columns mx-3">
              {snippets.map((snippet) => (
                <SnippetCard
                  key={snippet.id}
                  snippet={snippet}
                  handleFav={onSnippetFav}
                  userIsLogged={userIsLogged}
                />
              ))}
            </div>
            <div className="flex-center">
              <Pagination
                currentPage={currentPage}
                links={links}
                onPageTransition={onPageTransition}
                onPageTransitionWithPage={onPageTransitionWithPage}
                className="mx-auto"
              />
            </div>
          </React.Fragment>
        ))}
    </div>
  );
};

export default SnippetFeed;
