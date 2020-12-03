import SnippetFeedClient from "../../api/implementations/SnippetFeedClient";
import SnippetFeedHOC from "./snippet_feed_hoc";
import { getExploreSearchFromUrl } from "../../js/search_from_url";
import SnippetExplore from "../explore/snippet_explore";

const Explore = SnippetFeedHOC(
  SnippetExplore,
  (SnippetFeedClient, page) => SnippetFeedClient.getHomeSnippetFeed(page),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.exploreSnippetFeed(page, search),
  (url) => getExploreSearchFromUrl(url)
);

export default Explore;
