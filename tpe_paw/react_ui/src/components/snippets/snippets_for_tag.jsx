import React, { Component } from "react";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import LanguagesAndTagsActionsClient from "../../api/implementations/LanguagesAndTagsActionsClient";
import i18n from "../../i18n";
import store from "../../store";
import { Badge } from "reactstrap";
import SnippetFeed from "../snippets/snippet_feed";

class SnippetsForTag extends Component {
  languagesAndTagsClient;
  languagesAndTagsActionsClient;

  constructor(props) {
    super(props);
    const state = store.getState();
    let loggedUserId = null;
    this.languagesAndTagsClient = new LanguagesAndTagsClient();
    this.languagesAndTagsActionsClient = new LanguagesAndTagsActionsClient();
    if (!(state.auth.token === null || state.auth.token === undefined)) {
      this.languagesAndTagsClient = new LanguagesAndTagsClient(
        this.props,
        state.auth.token
      );
      this.languagesAndTagsActionsClient = new LanguagesAndTagsActionsClient(
        this.props,
        state.auth.token
      );
      loggedUserId = state.auth.info.uid;
    }
    this.state = {
      loggedUserId: loggedUserId,
      tag: { id: -1, name: "..." },
      follows: false,
    };
  }

  _loadTagInfo() {
    this.languagesAndTagsClient
      .getTagWithId(this.props.match.params.id)
      .then((res) => {
        this.setState({ tag: res.data });
      });
  }

  _userIsFollowing() {
    console.log(this.props.match.params.id);
    if (this.state.loggedUserId !== null) {
      this.languagesAndTagsClient
        .userFollowsTag(this.state.loggedUserId, this.props.match.params.id)
        .then((res) => {
          this.setState({ follows: res.data.aBoolean });
        })
        .catch((e) => {});
    }
  }

  componentDidMount() {
    this._loadTagInfo();
    this._userIsFollowing();
  }

  _enforceUserLogged() {
    // If user is not logged, redirect to login
    if (!this.props.userIsLogged) {
      this.props.history.push({
        pathname: "/login",
        state: { from: this.props.history.location },
      });
    }
  }

  _onFollow(e) {
    this._enforceUserLogged();

    if (this.props.userIsLogged) {
      let previousFollowState = this.state.follows;
      this.setState({ follows: !previousFollowState });

      // Was faved, now not
      if (previousFollowState) {
        this.languagesAndTagsActionsClient
          .unfollowTag(this.props.match.params.id)
          .then((res) => {})
          .catch((e) => {});
      }
      // Was not fav, not it is
      else {
        this.languagesAndTagsActionsClient
          .followTag(this.props.match.params.id)
          .then((res) => {})
          .catch((e) => {});
      }

      // Prevent parent Link to navigate
      e.preventDefault();
    }
  }

  render() {
    const {
      snippets,
      totalSnippets,
      links,
      currentPage,
      onPageTransition,
      onSnippetFav,
      userIsLogged,
    } = this.props;
    return (
      <div className="flex-center flex-column">
        <div className="flex-center flex-row mt-4 mb-2">
          <h1 className="fw-500 no-margin">
            {i18n.t("items.snippetsFor", {
              item: this.state.tag.name.toUpperCase(),
            })}
          </h1>
          <div className="mx-3"></div>
          <Badge
            onClick={(e) => this._onFollow(e)}
            className={"flex-center tag-action-badge-snippets "}
            color="secondary"
          >
            {this.state.follows
              ? i18n.t("items.unfollow")
              : i18n.t("items.follow")}
          </Badge>
        </div>
        <h5 className="fw-100 mb-3">
          ({i18n.t("snippetWithNumber", { count: totalSnippets })})
        </h5>

        <SnippetFeed
          snippets={snippets}
          links={links}
          currentPage={currentPage}
          onPageTransition={onPageTransition}
          onSnippetFav={onSnippetFav}
          userIsLogged={userIsLogged}
        />
      </div>
    );
  }
}

export default SnippetsForTag;
