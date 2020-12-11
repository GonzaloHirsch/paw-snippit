import Client from "../Client";
import axios from "axios";

export default class LanguagesAndTagsClient extends Client {
  // LANGUAGES
  // ------------------------------------------------------------
  getLanguageWithId(id) {
    return this.instance.get("languages/" + id);
  }

  getLanguageList() {
    return this.instance.get("languages/all");
  }

  getLanguagesFeed(page) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get("languages?" + params.toString());
  }

  searchLanguagesFeed(page, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get("languages/search?" + params.toString());
  }

  getLanguageWithUrl(url) {
    return axios.get(url);
  }

  // TAGS
  // ------------------------------------------------------------

  getTagWithId(id) {
    return this.instance.get("tags/" + id);
  }

  getTagList() {
    return this.instance.get("tags/all");
  }

  getTagsFeed(page) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get("tags?" + params.toString());
  }

  searchTagsFeed(page, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get("tags/search?" + params.toString());
  }

  getTagWithUrl(url) {
    return axios.get(url);
  }

  getTagListWithUrl(url) {
    return axios.get(url);
  }
}
