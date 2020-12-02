import Client from "../Client";
import axios from "axios";

export default class SnippetFeedClient extends Client {
  getHomeSnippetFeed(page) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get("snippets?" + params.toString());
  }

  searchHomeSnippetFeed(page, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get("snippets/search?" + params.toString());
  }

  getFavoritesSnippetFeed(page, userId) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get(
      "users/" + userId + "/favorite_snippets?" + params.toString()
    );
  }

  searchFavoritesSnippetFeed(page, userId, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get(
      "users/" + userId + "/favorite_snippets/search?" + params.toString()
    );
  }

  getUpvotedSnippetFeed(page, userId) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get(
      "users/" + userId + "/upvoted_snippets?" + params.toString()
    );
  }

  searchUpvotedSnippetFeed(page, userId, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get(
      "users/" + userId + "/upvoted_snippets/search?" + params.toString()
    );
  }

  getFollowingSnippetFeed(page) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get("snippets/following?" + params.toString());
  }

  getFlaggedSnippetFeed(page) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get("snippets/flagged?" + params.toString());
  }

  getSnippetFeedWithUrl(url) {
    return axios.get(url);
  }
}
