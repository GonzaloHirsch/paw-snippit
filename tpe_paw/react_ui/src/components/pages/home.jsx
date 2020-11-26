import React, { Component } from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import SnippetFeedWithContext from "../snippets/snippet_feed_with_context";
import SnippetFeedHOC from "./snippet_feed_hoc";

const Home = SnippetFeedHOC(
  SnippetFeedWithContext,
  "Home",
  (SnippetFeedClient, page) => SnippetFeedClient.getHomeSnippetFeed(page)
);

export default Home;
