import React, { Component } from "react";
import SnippetCard from "./snippet_card";
import Pagination from "../navigation/pagination";

// Stateless Functional Component

const SnippetFeed = (props) => {
  const { snippets, page, links, onPageTransition } = props;
  return (
    <React.Fragment>
      <div className="card-columns mx-3">
        {snippets.map((snippet) => (
          <SnippetCard key={snippet.id} snippet={snippet} />
        ))}
      </div>
      <div className="flex-center">
        <Pagination
          currentPage={page}
          links={links}
          onPageTransition={onPageTransition}
          className="mx-auto"
        />
      </div>
    </React.Fragment>
  );
};

export default SnippetFeed;
