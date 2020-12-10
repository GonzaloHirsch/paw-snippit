import Client from "../Client";
import axios from "axios";
import {
  ACTIVE_USER_SNIPPETS,
  DELETED_USER_SNIPPETS,
} from "../../js/constants";

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

  exploreSnippetFeed(page, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get("snippets/explore/search?" + params.toString());
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

  getProfileActiveSnippetFeed(page, userId) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get(
      "users/" + userId + "/active_snippets?" + params.toString()
    );
  }

  searchProfileActiveSnippetFeed(page, userId, search) {
    const params = new URLSearchParams({ page: page });

    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get(
      "users/" + userId + "/active_snippets/search?" + params.toString()
    );
  }

  getProfileDeletedSnippetFeed(page, userId) {
    const params = new URLSearchParams({ page: page });
    return this.instance.get(
      "users/" + userId + "/deleted_snippets?" + params.toString()
    );
  }

  searchProfileDeletedSnippetFeed(page, userId, search) {
    const params = new URLSearchParams({ page: page });

    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get(
      "users/" + userId + "/deleted_snippets/search?" + params.toString()
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

  searchFlaggedSnippetFeed(page, search) {
    const params = new URLSearchParams({ page: page });
    for (let key in search) {
      params.set(key, search[key]);
    }
    return this.instance.get("snippets/flagged/search?" + params.toString());
  }

  getSnippetFeedWithUrl(url) {
    return axios.get(url);
  }
}
