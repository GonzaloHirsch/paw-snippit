import React, { Component } from "react";
import SnippetDetail from "../snippets/snippet_detail";
import SnippetClient from "../../api/implementations/SnippetOverviewClient";
import UserClient from "../../api/implementations/UserClient";
import LanguageClient from "../../api/implementations/LanguageClient";
import store from "../../store";
import SnippetActionsClient from "../../api/implementations/SnippetActionsClient";
import { isAdmin } from "../../js/security_utils";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

class SnippetOverview extends Component {
  snippetClient;
  userClient;
  languageClient;
  snippetActionsClient;

  constructor(props) {
    super(props);

    const state = store.getState();
    let userIsLogged = false;
    let userCanReport = false;
    let userIsAdmin = false;
    let loggedUserId = -1;
    if (state.auth.token === null || state.auth.token === undefined) {
      this.snippetActionsClient = new SnippetActionsClient();
      this.snippetClient = new SnippetClient();
      this.userClient = new UserClient();
      this.languageClient = new LanguageClient();
    } else {
      this.snippetActionsClient = new SnippetActionsClient(state.auth);
      this.snippetClient = new SnippetClient(state.auth);
      this.userClient = new UserClient(state.auth);
      this.snippetActionsClient = new SnippetActionsClient(state.auth);
      userIsLogged = true;
      userCanReport = state.auth.info.canReport;
      userIsAdmin = isAdmin(state.auth.roles);
      loggedUserId = state.auth.info.uid;
    }

    this.onSnippetFav = this.onSnippetFav.bind(this);
    this.onSnippetFlag = this.onSnippetFlag.bind(this);
    this.onSnippetDelete = this.onSnippetDelete.bind(this);
    this.onReport = this.onReport.bind(this);
    this.dismissedReport = this.dismissedReport.bind(this);

    this.state = {
      snippet: {
        id: 0,
        title: "TITLE",
        description: "DESCRIPTION",
        code: "CODE",
        flagged: false,
        favorite: false,
        deleted: false,
        createdDate: "10/10/10",
      },
      creator: {
        username: "USERNAME",
        id: 0,
        picture: "/userIcon.jpg",
        hasPicture: false,
        reputation: 0,
      },
      language: {
        name: "LANGUAGE",
        id: 0,
      },
      userIsLogged: userIsLogged,
      userCanReport: userCanReport,
      userIsOwner: false,
      userIsAdmin: userIsAdmin,
      loggedUserId: loggedUserId
    };
  }

  loadSnippet() {
    const snippetId = parseInt(this.props.match.params.id, 10);

    this.snippetClient
      .getSnippetWithId(snippetId)
      .then((res) => {
        if (
          !(res.data.favorite || (res.data.creator.id === this.state.loggedUserId)) &&
          res.data.deleted
        ) {
          // TODO: REDIRECT TO 404
          this.props.history.push({
            pathname: "/login",
            state: { from: this.props.history.location },
          });
        }
        this.setState({ snippet: res.data });
        // If snippet was found, obtain the creator
        this.userClient
          .getUserWithUrl(this.state.snippet.creator.url)
          .then((res) => {
            const state = store.getState();
            let userIsOwner = false;
            if (
              state.auth.token !== null &&
              state.auth.token !== undefined &&
              res.data.id === state.auth.info.uid
            ) {
              userIsOwner = true;
            }
            this.setState({ creator: res.data, userIsOwner: userIsOwner });
          })
          .catch((e) => {});

        // If snippet was found, obtain the language
        this.languageClient
          .getLanguageWithUrl(this.state.snippet.language.url)
          .then((res) => {
            this.setState({ language: res.data });
          })
          .catch((e) => {});
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this.loadSnippet();
  }

  dismissedReport() {
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Update variable
    snippet.reported = false;
    // Update state
    this.setState({ snippet: snippet });
  }

  onSnippetFav(e, id) {
    // If user is not logged, redirect to login
    if (!this.state.userIsLogged) {
      this.props.history.push({
        pathname: "/login",
        state: { from: this.props.history.location },
      });
    }

    let previousFavState = false;
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Store previous fav state
    previousFavState = snippet.favorite;
    // Update variable
    snippet.favorite = !snippet.favorite;
    // Update state
    this.setState({ snippet: snippet });

    // Was faved, now not
    if (previousFavState) {
      this.snippetActionsClient.unfavSnippet(id);
    }
    // Was not fav, not it is
    else {
      this.snippetActionsClient.favSnippet(id);
    }

    // Prevent parent Link to navigate
    e.preventDefault();
  }

  onSnippetFlag(e, id) {
    let previousFlagState = false;
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Store previous fav state
    previousFlagState = snippet.flagged;
    // Update variable
    snippet.flagged = !snippet.flagged;
    // Update state
    this.setState({ snippet: snippet });

    // Was faved, now not
    if (previousFlagState) {
      this.snippetActionsClient.unflagSnippet(id);
    }
    // Was not fav, not it is
    else {
      this.snippetActionsClient.flagSnippet(id);
    }

    // Prevent parent Link to navigate
    e.preventDefault();
  }

  onSnippetDelete(e, id) {
    let previousDeleteState = false;
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Store previous fav state
    previousDeleteState = snippet.deleted;
    // Update variable
    snippet.deleted = !snippet.deleted;
    // Update state
    this.setState({ snippet: snippet });

    // Was faved, now not
    if (previousDeleteState) {
      this.snippetActionsClient.restoreSnippet(id);
    }
    // Was not fav, not it is
    else {
      this.snippetActionsClient.deleteSnippet(id);
    }

    // Prevent parent Link to navigate
    e.preventDefault();
  }

  onReport(id, detail) {
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Store previous fav state
    // Update variable
    snippet.userReported = true;
    // Update state
    this.setState({ snippet: snippet });

    this.snippetActionsClient.reportSnippet(id, detail);
  }

  render() {
    return (
      <div className="flex-center">
        <SnippetDetail
          {...this.state}
          dismissedReport={this.dismissedReport}
          handleFav={this.onSnippetFav}
          handleFlag={this.onSnippetFlag}
          handleReport={this.onReport}
          handleDelete={this.onSnippetDelete}
        />
      </div>
    );
  }
}

export default SnippetOverview;
