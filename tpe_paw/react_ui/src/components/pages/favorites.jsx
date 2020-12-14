import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetFavoriteFeed from "../snippets/snippet_favorite_feed";
import { getNavSearchFromUrl } from "../../js/search_from_url";
import store from "../../store";

const userId = store.getState().auth.info.uid;

const Favorites = SnippetFeedHOC(
  SnippetFavoriteFeed,
  (SnippetFeedClient, page) =>
    SnippetFeedClient.getFavoritesSnippetFeed(page, userId),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.searchFavoritesSnippetFeed(page, userId, search),
  (url) => getNavSearchFromUrl(url)
);

export default Favorites;
