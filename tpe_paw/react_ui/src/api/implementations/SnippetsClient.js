import Client from "../Client";
import axios from "axios";

export default class SnippetsClient extends Client {
  getSnippetFeed(page) {
    const params = new URLSearchParams([['page', page]]);
    return this.instance.get("snippets", params);
  }

  getSnippetFeedWithUrl(url){
    return axios.get(url);
  }
}
