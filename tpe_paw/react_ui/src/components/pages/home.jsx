import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetHomeFeed from "../snippets/snippet_home_feed";
import { getNavSearchFromUrl } from "../../js/search_from_url";

const Home = SnippetFeedHOC(
  SnippetHomeFeed,
  (SnippetFeedClient, page) => SnippetFeedClient.getHomeSnippetFeed(page),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.searchHomeSnippetFeed(page, search),
  (url) => getNavSearchFromUrl(url),
  false
);

export default Home;
