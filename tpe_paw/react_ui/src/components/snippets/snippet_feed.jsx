import React, { Component } from "react";
import SnippetCard from "./snippet_card";
import Pagination from "../navigation/pagination";

// Stateless Functional Component

const SnippetFeed = (props) => {
  const {
    snippets,
    currentPage,
    links,
    onPageTransition,
    onSnippetFav,
    userIsLogged,
  } = props;
  return (
    <div className="flex-column">
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
          className="mx-auto"
        />
      </div>
    </div>
  );
};

export default SnippetFeed;
