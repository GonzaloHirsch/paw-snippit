import React, { Component } from "react";
import ProfileDetail from "../profile/profile_detail";
import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetFeed from "../snippets/snippet_feed";
import { getNavSearchFromUrl } from "../../js/search_from_url";

import {
  ACTIVE_USER_SNIPPETS,
  DELETED_USER_SNIPPETS,
} from "../../js/constants";

class UserProfile extends Component {
  state = {
    context: ACTIVE_USER_SNIPPETS,
  };

  _renderContext() {
    if (this.state.context === ACTIVE_USER_SNIPPETS) {
      const ActiveSnippetFeed = SnippetFeedHOC(
        SnippetFeed,
        (SnippetFeedClient, page) =>
          SnippetFeedClient.getProfileActiveSnippetFeed(
            page,
            this.props.match.params.id
          ),
        (SnippetFeedClient, page, search) =>
          SnippetFeedClient.searchProfileActiveSnippetFeed(
            page,
            this.props.match.params.id,
            search
          ),
        (url) => getNavSearchFromUrl(url)
      );
      return <ActiveSnippetFeed />;
    } else if (this.state.context === DELETED_USER_SNIPPETS) {
      const DeletedSnippetFeed = SnippetFeedHOC(
        SnippetFeed,
        (SnippetFeedClient, page) =>
          SnippetFeedClient.getProfileDeletedSnippetFeed(
            page,
            this.props.match.params.id
          ),
        (SnippetFeedClient, page, search) =>
          SnippetFeedClient.searchProfileDeletedSnippetFeed(
            page,
            this.props.match.params.id,
            search
          ),
        (url) => getNavSearchFromUrl(url)
      );
      return <DeletedSnippetFeed />;
    }
  }

  render() {
    return this._renderContext();
  }
}

export default UserProfile;
