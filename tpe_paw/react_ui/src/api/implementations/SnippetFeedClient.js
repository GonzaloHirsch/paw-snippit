import Client from "../Client";
import axios from "axios";

export default class SnippetFeedClient extends Client {
  getHomeSnippetFeed(page) {
    const params = new URLSearchParams({"page": page});
    return this.instance.get("snippets?" + params.toString());
  }

  getFavoritesSnippetFeed(page, userId) {
    const params = new URLSearchParams({"page": page});
    return this.instance.get("users/" + userId + "/favorite_snippets?" + params.toString());
  }

  getUpvotedSnippetFeed(page) {
    const params = new URLSearchParams({"page": page});
    return this.instance.get("snippets/upvoted?" + params.toString());
  }

  getFollowingSnippetFeed(page) {
    const params = new URLSearchParams({"page": page});
    return this.instance.get("snippets/following?" + params.toString());
  }

  getFlaggedSnippetFeed(page) {
    const params = new URLSearchParams({"page": page});
    return this.instance.get("snippets/flagged?" + params.toString());
  }

  getSnippetFeedWithUrl(url) {
    return axios.get(url);
  }
}
