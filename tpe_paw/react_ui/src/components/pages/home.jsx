import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetHomeFeed from "../snippets/snippet_home_feed";

const Home = SnippetFeedHOC(
  SnippetHomeFeed,
  (SnippetFeedClient, page) => SnippetFeedClient.getHomeSnippetFeed(page),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.searchHomeSnippetFeed(page, search)
);

export default Home;
