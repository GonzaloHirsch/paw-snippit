import React, { Component } from "react";
import ProfileDetail from "../profile/profile_detail";
import ProfileVerifyMessage from "../profile/profile_verify_message";
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
import SnippetProfileFeed from "../snippets/snippet_profile_feed";
import { Helmet } from "react-helmet";

class UserProfile extends Component {
  userClient;
  protectedClient;
  ActiveSnippetFeed;
  DeletedSnippetFeed;

  constructor(props) {
    super(props);
    const state = store.getState();
    let loggedUserId = null;
    this.userClient = new UserClient(this.props);
    if (!(state.auth.token === null || state.auth.token === undefined)) {
      this.protectedClient = new UserClient(this.props, state.auth.token);
      loggedUserId = state.auth.info.uid;
    }

    let ownerId = parseInt(
      this.props.match === undefined || this.props.match === null
        ? loggedUserId
        : this.props.match.params.id
    );

    // Generating feed instances
    this.instantiateFeedHOCs(ownerId);

    this.state = {
      loggedUserId: loggedUserId,
      context: ACTIVE_USER_SNIPPETS,
      profileOwner: [],
      profileOwnerId: ownerId,
      loading: true,
      descriptionLoading: false,
      imageLoading: false,
      errors: {
        image: null,
      },
    };
  }

  instantiateFeedHOCs(id) {
    this.ActiveSnippetFeed = SnippetFeedHOC(
      SnippetProfileFeed,
      (SnippetFeedClient, page) =>
        SnippetFeedClient.getProfileActiveSnippetFeed(
          page,
          id
        ),
      (SnippetFeedClient, page, search) =>
        SnippetFeedClient.searchProfileActiveSnippetFeed(
          page,
          id,
          search
        ),
      (url) => getNavSearchFromUrl(url)
    );

    this.DeletedSnippetFeed = SnippetFeedHOC(
      SnippetFeed,
      (SnippetFeedClient, page) =>
        SnippetFeedClient.getProfileDeletedSnippetFeed(
          page,
          id
        ),
      (SnippetFeedClient, page, search) =>
        SnippetFeedClient.searchProfileDeletedSnippetFeed(
          page,
          id,
          search
        ),
      (url) => getNavSearchFromUrl(url)
    );
  }

  loadUserData(id) {
    this.userClient
      .getUserWithId(id)
      .then((res) => {
        this.setState({
          profileOwner: res.data,
          loading: false,
          profileOwnerId: id,
        });
      })
      .catch((e) => {
        if (e.response) {
          // User not found, go to 404
          if (e.response.status === 404) {
            this.props.history.push("/404");
          }
        }
      });
  }

  componentDidMount() {
    this.loadUserData(this.state.profileOwnerId);
  }

  UNSAFE_componentWillReceiveProps(nextProps) {
    if (nextProps.match !== null && nextProps.match !== undefined) {
      const userId = parseInt(nextProps.match.params.id, 10);
      if (userId !== this.state.profileOwnerId) {
        this.loadUserData(userId);
        // Generating feed instances
        this.instantiateFeedHOCs(userId);
      }
    }
  }

  // Handlers
  onTabChange = (context) => {
    this.setState({ context: context });
  };

  onUpdateDescription = (description) => {
    this.setState({ descriptionLoading: true });
    this.protectedClient
      .putUserDescription(this.state.profileOwnerId, description)
      .then((res) => {
        const owner = { ...this.state.profileOwner };
        owner.description = description;
        this.setState({ profileOwner: owner, descriptionLoading: false });
      })
      .catch((e) => {});
  };

  onUpdateImage = (image) => {
    this.setState({ imageLoading: true });
    this.protectedClient
      .putUserImage(this.state.profileOwnerId, image)
      .then((res) => {
        this.setState({ imageLoading: false, errors: { image: null } });
        this.loadUserData(this.state.profileOwnerId);
      })
      .catch((e) => {
        let errors = {
          image: null,
        };
        if (e.response) {
          // client received an error response (5xx, 4xx)
          if (e.response.status === 400) {
            errors.image = i18n.t("profile.errors.imageTooBig", {
              size: "1MB",
            });
          } else if (e.response.status === 500) {
            errors.image = i18n.t("profile.errors.changeImageServerError");
          }
        } else if (e.request) {
          // client never received a response, or request never left
          errors.image = i18n.t("errors.noConnection");
        } else {
          // anything else
          errors.image = i18n.t("errors.unknownError");
        }
        this.setState({
          errors: errors,
          imageLoading: false,
        });
      });
  };

  isOwner = () => {
    return this.state.profileOwnerId === this.state.loggedUserId;
  };

  _renderVerifyMessage() {
    return this.state.loggedUserId === this.state.profileOwnerId &&
      !this.state.profileOwner.verified ? (
      <div className="flex-center my-2">
        <ProfileVerifyMessage id={this.state.profileOwnerId} />
      </div>
    ) : null;
  }

  // Render methods
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
      return (
        <this.ActiveSnippetFeed
          isOwner={this.state.profileOwnerId === this.state.loggedUserId}
        />
      );
    } else if (this.state.context === DELETED_USER_SNIPPETS) {
      return <this.DeletedSnippetFeed />;
    }
  }

  render() {
    return (
      <div className="row parent-height">
        <Helmet>
          <title>
            {this.isOwner()
              ? i18n.t("nav.profile")
              : i18n.t("app") + " | " + this.state.profileOwner.username}
          </title>
        </Helmet>
        <div className="col-3 profile-user-data d-flex flex-col align-items-center">
          <ProfileDetail
            owner={this.state.profileOwner}
            loggedUserId={this.state.loggedUserId}
            loading={this.state.loading}
            descriptionLoading={this.state.descriptionLoading}
            imageLoading={this.state.imageLoading}
            imageRequestErrors={this.state.errors.image}
            updateDescription={(description) =>
              this.onUpdateDescription(description)
            }
            updateImage={(img) => this.onUpdateImage(img)}
          />
        </div>
        <div className="col-9 flex-column">
          {this._renderVerifyMessage()}
          {this._renderTabs()}
          <div className="py-3 background-color profile-snippet-container rounded-border">
            {this._renderFeedContext()}
          </div>
        </div>
      </div>
    );
  }
}

export default UserProfile;
