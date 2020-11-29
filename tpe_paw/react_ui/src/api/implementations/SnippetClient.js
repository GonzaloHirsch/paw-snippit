import Client from "../Client";
import axios from "axios";

export default class SnippetFeedClient extends Client {
  getSnippetWithId(id) {
    return this.instance.get("snippets/" + id);
  }

  getSnippetWithUrl(url) {
    return axios.get(url);
  }
}
