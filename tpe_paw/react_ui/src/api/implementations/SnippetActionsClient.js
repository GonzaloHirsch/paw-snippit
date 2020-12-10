import Client from "../Client";
import axios from "axios";

export default class SnippetActionsClient extends Client {
  deleteSnippet(id) {
    return this.instance.delete("snippets/" + id + "/delete");
  }

  restoreSnippet(id) {
    return this.instance.put("snippets/" + id + "/restore");
  }

  getSnippetVotes(id){
    return this.instance.get("snippets/" + id + "/vote_count")
  }

  voteSnippetPositive(id) {
    return this.instance.put("snippets/" + id + "/vote_positive");
  }

  unvoteSnippetPositive(id) {
    return this.instance.delete("snippets/" + id + "/vote_positive");
  }

  voteSnippetNegative(id) {
    return this.instance.put("snippets/" + id + "/vote_negative");
  }

  unvoteSnippetNegative(id) {
    return this.instance.delete("snippets/" + id + "/vote_negative");
  }

  flagSnippet(id) {
    return this.instance.put("snippets/" + id + "/flag");
  }

  unflagSnippet(id) {
    return this.instance.delete("snippets/" + id + "/flag");
  }

  favSnippet(id) {
    return this.instance.put("snippets/" + id + "/fav");
  }

  unfavSnippet(id) {
    return this.instance.delete("snippets/" + id + "/fav");
  }

  reportSnippet(id, detail) {
    return this.instance.put("snippets/" + id + "/report", {
      reportDetail: detail,
    });
  }

  dismissReport(id) {
    return this.instance.put("snippets/" + id + "/report/dismiss");
  }
}
