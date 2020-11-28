import Client from "../Client";
import axios from "axios";

export default class SnippetFeedClient extends Client {
  getSnippetWithId(id) {
    // const params = new URLSearchParams([["id", id]]);
    return this.instance.get("snippets/" + id);
  }

  getSnippetWithUrl(url) {
    return axios.get(url);
  }
}
