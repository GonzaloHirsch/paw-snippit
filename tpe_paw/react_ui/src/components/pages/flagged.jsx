import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetFlaggedFeed from "../snippets/snippet_flagged_feed";
import { getNavSearchFromUrl } from "../../js/search_from_url";

const Flagged = SnippetFeedHOC(
  SnippetFlaggedFeed,
  (SnippetFeedClient, page) => SnippetFeedClient.getFlaggedSnippetFeed(page),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.searchFlaggedSnippetFeed(page, search),
  (url) => getNavSearchFromUrl(url),
  true
);

export default Flagged;
