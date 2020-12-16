import SnippetFeedHOC from "./snippet_feed_hoc";
import { getNavSearchFromUrl } from "../../js/search_from_url";
import SnippetFollowing from "../snippets/snippet_following";
import store from "../../store";

const userId = store.getState().auth.info.uid;

const Following = SnippetFeedHOC(
  SnippetFollowing,
  (SnippetFeedClient, page) =>
    SnippetFeedClient.getFollowingSnippetFeed(page, userId),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.searchFollowingSnippetFeed(page, search, userId),
  (url) => getNavSearchFromUrl(url)
);

export default Following;
