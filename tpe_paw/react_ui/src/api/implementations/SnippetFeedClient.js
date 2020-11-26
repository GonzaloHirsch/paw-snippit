import Client from "../Client";
import axios from "axios";

export default class SnippetFeedClient extends Client {
  getHomeSnippetFeed(page) {
    const params = new URLSearchParams([["page", page]]);
    return this.instance.get("snippets", params);
  }

  getFavoritesSnippetFeed(page) {
    const params = new URLSearchParams([["page", page]]);
    return this.instance.get("snippets/favorites", params);
  }

  getUpvotedSnippetFeed(page) {
    const params = new URLSearchParams([["page", page]]);
    return this.instance.get("snippets/upvoted", params);
  }

  getFollowingSnippetFeed(page) {
    const params = new URLSearchParams([["page", page]]);
    return this.instance.get("snippets/following", params);
  }

  getFlaggedSnippetFeed(page) {
    const params = new URLSearchParams([["page", page]]);
    return this.instance.get("snippets/flagged", params);
  }

  getSnippetFeedWithUrl(url) {
    return axios.get(url);
  }
}
