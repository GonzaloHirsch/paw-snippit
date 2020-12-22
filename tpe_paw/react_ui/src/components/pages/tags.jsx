import ItemFeedHOC from "./item_feed_hoc";
import TagsFeed from "../items/tags_feed";
import { getTagsSearchFromUrl } from "../../js/search_from_url";
import store from "../../store";

const state = store.getState();
let userIsLogged = false;
if (!(state.auth.token === null || state.auth.token === undefined)) {
    userIsLogged = true;
}

const Tags = ItemFeedHOC(
  TagsFeed,
  (TagsClient, page) => TagsClient.getTagsFeed(page),
  (TagsClient, page, search) => TagsClient.searchTagsFeed(page, search),
  (url) => getTagsSearchFromUrl(url),
  userIsLogged
);

export default Tags;
