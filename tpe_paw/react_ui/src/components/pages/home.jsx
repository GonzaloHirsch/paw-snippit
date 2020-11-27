import React, { Component } from "react";
import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetHomeFeed from "../snippets/snippet_home_feed";

const Home = SnippetFeedHOC(SnippetHomeFeed, (SnippetFeedClient, page) =>
  SnippetFeedClient.getHomeSnippetFeed(page)
);

export default Home;
