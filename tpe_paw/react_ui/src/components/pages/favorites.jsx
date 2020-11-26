import React, { Component } from "react";
import SnippetFeedWithContext from "../snippets/snippet_feed_with_context";
import SnippetFeedHOC from "./snippet_feed_hoc";

const Favorites = SnippetFeedHOC(
  SnippetFeedWithContext,
  "Favorites",
  (SnippetFeedClient, page) => SnippetFeedClient.getFavoritesSnippetFeed(page)
);

export default Favorites;
