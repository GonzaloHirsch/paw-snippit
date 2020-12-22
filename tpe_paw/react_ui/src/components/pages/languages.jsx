import ItemFeedHOC from "./item_feed_hoc";
import LanguagesFeed from "../items/languages_feed";
import { getLanguagesSearchFromUrl } from "../../js/search_from_url";

const Languages = ItemFeedHOC(
  LanguagesFeed,
  (LanguagesClient, page) => LanguagesClient.getLanguagesFeed(page),
  (LanguagesClient, page, search) =>
    LanguagesClient.searchLanguagesFeed(page, search),
  (url) => getLanguagesSearchFromUrl(url),
  false
);

export default Languages;
