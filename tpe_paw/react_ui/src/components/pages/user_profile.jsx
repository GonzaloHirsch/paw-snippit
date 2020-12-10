import React, { Component } from "react";
import ProfileDetail from "../profile/profile_detail";
import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetFeed from "../snippets/snippet_feed";
import { getNavSearchFromUrl } from "../../js/search_from_url";
import store from "../../store";
import UserClient from "../../api/implementations/UserClient";
import i18n from "../../i18n";
import {
  ACTIVE_USER_SNIPPETS,
  DELETED_USER_SNIPPETS,
} from "../../js/constants";

class UserProfile extends Component {
  userClient;

  constructor(props) {
    super(props);
    const state = store.getState();
    let loggedUserId = null;
    if (state.auth.token === null || state.auth.token === undefined) {
      this.userClient = new UserClient();
    } else {
      this.userClient = new UserClient(state.auth);
      loggedUserId = state.auth.info.uid;
    }
    this.state = {
      loggedUserId: loggedUserId,
      context: ACTIVE_USER_SNIPPETS,
      profileOwner: [],
      profileOwnerId: parseInt(
        this.props.match === undefined || this.props.match === null
          ? loggedUserId
          : this.props.match.params.id
      ),
      loading: true,
    };
  }

  loadUserData() {
    this.userClient
      .getUserWithId(this.state.profileOwnerId)
      .then((res) => {
        this.setState({
          profileOwner: res.data,
          loading: false,
        });
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this.loadUserData();
  }

  onTabChange = (context) => {
    this.setState({ context: context });
  };

  _renderTabs() {
    if (this.state.profileOwnerId !== this.state.loggedUserId) {
      return null;
    }
    return (
      <div className="px-3">
        <ul className="nav nav-tabs">
          <li className="nav-item profile-tabs-width">
            <button
              className={
                "parent-width nav-link profile-tabs " +
                (this.state.context !== ACTIVE_USER_SNIPPETS
                  ? "fw-300 profile-tabs-unselected"
                  : "fw-500 active")
              }
              onClick={() => this.onTabChange(ACTIVE_USER_SNIPPETS)}
            >
              {i18n.t("profile.activeSnippets")}
            </button>
          </li>
          <li className="nav-item profile-tabs-width">
            <button
              className={
                "parent-width nav-link profile-tabs " +
                (this.state.context !== DELETED_USER_SNIPPETS
                  ? "fw-300 profile-tabs-unselected"
                  : "fw-500 active")
              }
              onClick={() => this.onTabChange(DELETED_USER_SNIPPETS)}
            >
              {i18n.t("profile.deletedSnippets")}
            </button>
          </li>
        </ul>
      </div>
    );
  }

  _renderFeedContext() {
    if (this.state.context === ACTIVE_USER_SNIPPETS) {
      const ActiveSnippetFeed = SnippetFeedHOC(
        SnippetFeed,
        (SnippetFeedClient, page) =>
          SnippetFeedClient.getProfileActiveSnippetFeed(
            page,
            this.state.profileOwnerId
          ),
        (SnippetFeedClient, page, search) =>
          SnippetFeedClient.searchProfileActiveSnippetFeed(
            page,
            this.state.profileOwnerId,
            search
          ),
        (url) => getNavSearchFromUrl(url)
      );
      return <ActiveSnippetFeed background={"hello"} />;
    } else if (this.state.context === DELETED_USER_SNIPPETS) {
      const DeletedSnippetFeed = SnippetFeedHOC(
        SnippetFeed,
        (SnippetFeedClient, page) =>
          SnippetFeedClient.getProfileDeletedSnippetFeed(
            page,
            this.state.profileOwnerId
          ),
        (SnippetFeedClient, page, search) =>
          SnippetFeedClient.searchProfileDeletedSnippetFeed(
            page,
            this.state.profileOwnerId,
            search
          ),
        (url) => getNavSearchFromUrl(url)
      );
      return <DeletedSnippetFeed />;
    }
  }

  render() {
    return (
      <div className="row parent-height">
        <div className="col-3 profile-user-data d-flex flex-col align-items-center">
          <ProfileDetail
            owner={this.state.profileOwner}
            loading={this.state.loading}
          />
        </div>
        <div className="col-9 flex-column">
          {this._renderTabs()}
          <div className="pt-3 background-color profile-snippet-container rounded-border">
            {this._renderFeedContext()}
          </div>
        </div>
      </div>
    );
  }
}

export default UserProfile;
