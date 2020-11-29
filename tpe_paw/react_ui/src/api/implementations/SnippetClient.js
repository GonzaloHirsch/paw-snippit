import Client from "../Client";
import axios from "axios";

export default class SnippetOverviewClient extends Client {
  getSnippetWithId(id) {
    return this.instance.get("snippets/" + id);
  }

  getSnippetWithUrl(url) {
    return axios.get(url);
  }
}
