import React, { Component } from "react";
import SnippetFeed from "./snippet_feed";
import i18n from "../../i18n";
import LanguagesAndTagsActionsClient from "../../api/implementations/LanguagesAndTagsActionsClient";
import UserClient from "../../api/implementations/UserClient";
import store from "../../store";
import TagBadge from "../items/tag_badge";
import { getItemPositionInArray } from "../../js/item_utils";
import { Alert } from "reactstrap";

// Stateless Functional Component
class SnippetFollowing extends Component {
  tagActionClient;
  userClient;

  constructor(props) {
    super(props);
    const state = store.getState();
    if (state.auth.token === null || state.auth.token === undefined) {
      // ERROR should not be here if you are not logged in
    }
    this.tagActionClient = new LanguagesAndTagsActionsClient(
      this.props,
      state.auth.token
    );
    this.userClient = new UserClient(this.props, state.auth.token);
    this.state = {
      followingTags: [],
      userId: state.auth.info.uid,
      unfollowError: {
        alert: false,
        tagName: "",
      },
    };
  }

  loadFollowingTags() {
    this.userClient.getUserFollowingTags(this.state.userId).then((res) => {
      this.setState({ followingTags: res.data });
      console.log(this.state);
    });
  }

  componentDidMount() {
    this.loadFollowingTags();
  }

  onUnfollowTag = (tag) => {
    const followingList = [...this.state.followingTags];
    const index = getItemPositionInArray(followingList, tag.id);
    followingList.splice(index, 1);
    this.setState({ followingTags: followingList });
    this.tagActionClient.unfollowTag(tag.id).catch((e) => {
      const unfollowError = { alert: true, name: tag.name };
      this.setState({ unfollowError: unfollowError });
    });
  };

  onDismiss = () => {
    const unfollowError = { alert: false, name: "" };
    this.setState({ unfollowError: unfollowError });
  };

  render() {
    const {
      totalSnippets,
      snippets,
      links,
      currentPage,
      onPageTransition,
      onSnippetFav,
      userIsLogged,
      loading,
    } = this.props;
    return (
      <React.Fragment>
        <div className="mx-3 mb-3 fw-100">
          <h1 className="fw-300">{i18n.t("nav.following")}</h1>
          <h5 className="fw-100">
            ({totalSnippets} {i18n.t("snippets")})
          </h5>
        </div>
        <div className="row no-margin">
          <div className="col-2 p-0 pl-2 flex-col">
            {this.state.followingTags.map((tag) => (
              <TagBadge
                key={tag.id}
                tag={tag}
                onUnfollow={(tag) => this.onUnfollowTag(tag)}
              />
            ))}
          </div>

          <div className="col-10 p-0">
            <SnippetFeed
              snippets={snippets}
              totalSnippets={totalSnippets}
              links={links}
              currentPage={currentPage}
              onPageTransition={onPageTransition}
              onSnippetFav={onSnippetFav}
              userIsLogged={userIsLogged}
              loading={loading}
            />
          </div>
        </div>
        <Alert
          color="danger"
          className="shadow flex-center custom-alert"
          isOpen={this.state.unfollowError.alert}
          toggle={() => this.onDismiss()}
        >
          {i18n.t("following.errorMsg", {
            name: this.state.unfollowError.name,
          })}
        </Alert>
      </React.Fragment>
    );
  }
}

export default SnippetFollowing;
