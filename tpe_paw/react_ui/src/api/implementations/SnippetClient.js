import Client from "../Client";
import axios from "axios";

export default class SnippetClient extends Client {
  getSnippetWithId(id) {
    return this.instance.get("snippets/" + id);
  }

  getSnippetWithUrl(url) {
    return axios.get(url);
  }

  postCreateSnippet(snippet) {
    return this.instance.post("snippets", {
      title: snippet.title,
      description: snippet.description,
      code: snippet.code,
      language: snippet.language,
      tags: snippet.tags,
    });
  }
}
