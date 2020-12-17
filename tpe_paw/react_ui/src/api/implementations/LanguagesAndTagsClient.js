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

  postAddLanguage(name) {
    return this.instance.post("languages", { name: name });
  }

  getLanguageExists(name) {
    const params = new URLSearchParams({ name: name });
    return this.instance.get("languages/exists?" + params.toString());
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

  userFollowsTag(userId, tagId) {
    return this.instance.get("tags/" + tagId + "/users/" + userId + "/follows");
  }

  getTagWithUrl(url) {
    return axios.get(url);
  }

  getTagListWithUrl(url) {
    return axios.get(url);
  }

  postAddTag(name) {
    return this.instance.post("tags", { name: name });
  }

  getTagExists(name) {
    const params = new URLSearchParams({ name: name });
    return this.instance.get("tags/exists?" + params.toString());
  }
}
