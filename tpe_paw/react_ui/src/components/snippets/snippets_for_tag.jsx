import React, { Component } from "react";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import LanguagesAndTagsActionsClient from "../../api/implementations/LanguagesAndTagsActionsClient";
import i18n from "../../i18n";
import store from "../../store";
import { Badge } from "reactstrap";
import SnippetFeed from "../snippets/snippet_feed";
import { isAdmin } from "../../js/security_utils";
import { logOut } from "../../redux/actions/actionCreators";
import DeleteItemModal from "../items/delete_item_modal";
import { ITEM_TYPES } from "../../js/constants";
import { Helmet } from "react-helmet";


class SnippetsForTag extends Component {
  languagesAndTagsClient;
  languagesAndTagsActionsClient;

  constructor(props) {
    super(props);
    const state = store.getState();
    let loggedUserId = null;
    let userIsAdmin = false;
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
      userIsAdmin = isAdmin(state.auth.roles);
    }
    this.state = {
      loggedUserId: loggedUserId,
      tag: { id: -1, name: "...", deleted: false },
      follows: false,
      userIsAdmin: userIsAdmin,
      isDeleting: false,
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

  _enforceUserIsAdmin() {
    // If user is not logged, redirect to login
    if (!this.state.userIsAdmin) {
      store.dispatch(logOut());
      this.props.history.push("/login");
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

  _onDelete = (e) => {
    this._enforceUserLogged();
    this._enforceUserIsAdmin();

    // Updating the local state
    let tag = { ...this.state.tag };
    tag.deleted = true;
    this.setState({ tag: tag });

    this.languagesAndTagsActionsClient
      .deleteTag(tag.id)
      .then((res) => {})
      .catch((e) => {});

    this._setDeleting();

    // Redirect to tags again
    this.props.history.push("/tags");
  };

  _setDeleting = () => {
    this.setState({ isDeleting: !this.state.isDeleting });
  };

  render() {
    const {
      snippets,
      totalSnippets,
      links,
      currentPage,
      onPageTransition,
      onSnippetFav,
      userIsLogged,
      loading
    } = this.props;
    return (
      <React.Fragment>
        <div className="mt-2 ml-3 mb-3">
        <Helmet>
          <title>
            {i18n.t("nav.tags")} | {this.state.tag.name}
          </title>
        </Helmet>
          <div className="d-flex align-items-center flex-row mb-2">
            <DeleteItemModal
              item={this.state.tag}
              type={ITEM_TYPES.TAG}
              isOpen={this.state.isDeleting}
              onToggle={this._setDeleting}
              handleDeleteConfirm={this._onDelete}
            />
            <h1 className="no-margin fw-300">
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
            {this.state.userIsAdmin && !this.state.tag.deleted && (
              <React.Fragment>
                <div className="mx-2"></div>
                <Badge
                  onClick={this._setDeleting}
                  className={"flex-center tag-delete-badge-snippets "}
                  color="danger"
                >
                  {i18n.t("items.delete.actionName")}
                </Badge>
              </React.Fragment>
            )}
          </div>
          <h5 className="fw-100">
            ({i18n.t("snippetWithNumber", { count: totalSnippets })})
          </h5>
        </div>
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
      </React.Fragment>
    );
  }
}

export default SnippetsForTag;
