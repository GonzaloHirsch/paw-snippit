import React, { Component } from "react";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import LanguagesAndTagsActionsClient from "../../api/implementations/LanguagesAndTagsActionsClient";
import i18n from "../../i18n";
import SnippetFeed from "./snippet_feed";
import store from "../../store";
import ItemDeletedMessage from "../items/item_deleted_message";
import { Badge } from "reactstrap";
import { isAdmin } from "../../js/security_utils";
import { logOut } from "../../redux/actions/actionCreators";
import DeleteItemModal from "../items/delete_item_modal";
import { ITEM_TYPES } from "../../js/constants";
import { Helmet } from "react-helmet";

class SnippetsForLanguage extends Component {
  languagesAndTagsClient;
  languagesAndTagsActionsClient;

  constructor(props) {
    super(props);

    const state = store.getState();
    this.languagesAndTagsClient = new LanguagesAndTagsClient(this.props);
    this.languagesAndTagsActionsClient = new LanguagesAndTagsActionsClient(
      this.props
    );

    let userIsAdmin = false;
    if (!(state.auth.token === null || state.auth.token === undefined)) {
      this.languagesAndTagsActionsClient = new LanguagesAndTagsActionsClient(
        this.props,
        state.auth.token
      );
      userIsAdmin = isAdmin(state.auth.roles);
    }

    this.state = {
      language: { id: -1, name: "...", deleted: false },
      userIsAdmin: userIsAdmin,
      isDeleting: false,
    };
  }

  _loadLanguageInfo() {
    this.languagesAndTagsClient
      .getLanguageWithId(this.props.match.params.id)
      .then((res) => {
        this.setState({ language: res.data });
      });
  }

  _enforceUserLogged() {
    // If user is not logged, redirect to login
    if (!this.props.userIsLogged) {
      this.props.history.push({
        pathname: "/login",
        state: { from: this.props.history.location },
      });
      return false;
    }
    return true;
  }

  _enforceUserIsAdmin() {
    // If user is not logged, redirect to login
    if (!this.state.userIsAdmin) {
      store.dispatch(logOut());
      this.props.history.push({
        pathname: "/login",
        state: { from: this.props.history.location },
      });
      return true;
    }
    return false;
  }

  _onDelete = (e) => {
    if (this._enforceUserLogged()) return;
    if (this._enforceUserIsAdmin()) return;

    // Updating the local state
    let language = { ...this.state.language };
    language.deleted = true;
    this.setState({ language: language });

    this.languagesAndTagsActionsClient
      .deleteLanguage(language.id)
      .then((res) => {})
      .catch((e) => {});

    this._setDeleting();
  };

  _setDeleting = () => {
    this.setState({ isDeleting: !this.state.isDeleting });
  };

  componentDidMount() {
    this._loadLanguageInfo();
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
      loading,
    } = this.props;
    return (
      <div className="flex-center flex-column">
        <Helmet>
          <title>
            {i18n.t("nav.languages")} | {this.state.language.name}
          </title>
        </Helmet>
        {this.state.language.deleted && <ItemDeletedMessage />}
        <div className="flex-center mt-4 mb-2">
          <DeleteItemModal
            item={this.state.language}
            type={ITEM_TYPES.LANGUAGE}
            isOpen={this.state.isDeleting}
            onToggle={this._setDeleting}
            handleDeleteConfirm={this._onDelete}
          />
          <h1 className="fw-300 no-margin">
            {i18n.t("items.snippetsFor", {
              item: this.state.language.name.toUpperCase(),
            })}
          </h1>
          {this.state.userIsAdmin && !this.state.language.deleted && (
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
        <h5 className="fw-100 mb-3">
          ({i18n.t("snippetWithNumber", { count: totalSnippets })})
        </h5>

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
    );
  }
}

export default SnippetsForLanguage;
