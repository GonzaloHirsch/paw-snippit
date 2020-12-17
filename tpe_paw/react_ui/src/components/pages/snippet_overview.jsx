import React, { Component } from "react";
import SnippetDetail from "../snippets/snippet_detail";
import SnippetClient from "../../api/implementations/SnippetClient";
import UserClient from "../../api/implementations/UserClient";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import store from "../../store";
import SnippetActionsClient from "../../api/implementations/SnippetActionsClient";
import { isAdmin } from "../../js/security_utils";

// Higher Order Component to reuse the repeated behaviour of the pages that contain Snippet Feed

class SnippetOverview extends Component {
  snippetClient;
  userClient;
  languagesAndTagsClient;
  snippetActionsClient;

  constructor(props) {
    super(props);

    const state = store.getState();
    let userIsLogged = false;
    let userCanReport = false;
    let userIsAdmin = false;
    let loggedUserId = -1;
    this.userClient = new UserClient(this.props);
    this.languagesAndTagsClient = new LanguagesAndTagsClient(this.props);
    if (state.auth.token === null || state.auth.token === undefined) {
      this.snippetActionsClient = new SnippetActionsClient(this.props);
      this.snippetClient = new SnippetClient(this.props);
    } else {
      this.snippetActionsClient = new SnippetActionsClient(
        props,
        state.auth.token
      );
      this.snippetClient = new SnippetClient(props, state.auth.token);
      userIsLogged = true;
      userCanReport = state.auth.info.canReport;
      userIsAdmin = isAdmin(state.auth.roles);
      loggedUserId = state.auth.info.uid;
    }

    // Binding events
    this.onSnippetFav = this.onSnippetFav.bind(this);
    this.onSnippetFlag = this.onSnippetFlag.bind(this);
    this.onSnippetDelete = this.onSnippetDelete.bind(this);
    this.onReport = this.onReport.bind(this);
    this.dismissedReport = this.dismissedReport.bind(this);
    this.onSnippetLike = this.onSnippetLike.bind(this);
    this.onSnippetDislike = this.onSnippetDislike.bind(this);

    // Getting initial id
    const snippetId = parseInt(this.props.match.params.id, 10);

    this.state = {
      snippet: {
        id: snippetId,
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
        stats: {
          reputation: 0,
        },
      },
      language: {
        name: "LANGUAGE",
        id: 0,
      },
      tags: [],
      userIsLogged: userIsLogged,
      userCanReport: userCanReport,
      userIsOwner: false,
      userIsAdmin: userIsAdmin,
      loggedUserId: loggedUserId,
      loading: false,
    };
  }

  loadSnippet() {
    const snippetId = parseInt(this.props.match.params.id, 10);
    // Update the snippet id to avoid recursion of updates
    const oldSnippet = {...this.state.snippet};
    oldSnippet.id = snippetId; 
    this.setState({ loading: true, snippet: oldSnippet });
    const state = store.getState();

    this.snippetClient
      .getSnippetWithId(snippetId)
      .then((res) => {
        const snippet = res.data;
        if (
          !(
            snippet.favorite || snippet.creator.id === this.state.loggedUserId
          ) &&
          snippet.deleted
        ) {
          // TODO: REDIRECT TO 404
          this.props.history.push({
            pathname: "/login",
            state: { from: this.props.history.location },
          });
        }
        this.setState({ snippet: snippet, loading: false });

        Promise.all([
          this.userClient.getUserWithUrl(snippet.creator.url),
          this.languagesAndTagsClient.getLanguageWithUrl(snippet.language.url),
          this.languagesAndTagsClient.getWithUrl(snippet.tags),
        ]).then((values) => {
          let userIsOwner = false;
          if (
            state.auth.token !== null &&
            state.auth.token !== undefined &&
            values[0].data.id === state.auth.info.uid
          ) {
            userIsOwner = true;
          }

          this.setState({
            creator: values[0].data,
            userIsOwner: userIsOwner,
            language: values[1].data,
            tags: values[2].data,
            loading: false,
          });
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

  loadVotes() {
    this.snippetActionsClient
      .getSnippetVotes(this.state.snippet.id)
      .then((res) => {
        let snippet = { ...this.state.snippet };
        // Update variable
        snippet.voteCount = res.data.value;
        // Update state
        this.setState({ snippet: snippet });
      });
  }

  componentDidMount() {
    this.loadSnippet();
  }

  // In case the url changes
  componentDidUpdate() {
    const snippetId = parseInt(this.props.match.params.id, 10);
    if (snippetId !== this.state.snippet.id) {
      this.loadSnippet();
    }
  }

  dismissedReport() {
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Update variable
    snippet.reported = false;
    // Update state
    this.setState({ snippet: snippet });
  }

  enforceUserLogged() {
    // If user is not logged, redirect to login
    if (!this.state.userIsLogged) {
      this.props.history.push({
        pathname: "/login",
        state: { from: this.props.history.location },
      });
      return true;
    }
    return false;
  }

  onSnippetFav(e, id) {
    // If user is not logged, redirect to login
    if (this.enforceUserLogged()) return;

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

  onSnippetLike(e, id) {
    if (this.enforceUserLogged()) return;

    let previousLikeState = false;
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Store previous fav state
    previousLikeState = snippet.userVotedPositive;
    // Update variable
    snippet.userVotedPositive = !snippet.userVotedPositive;
    snippet.userVotedNegative = false;
    // Update state
    this.setState({ snippet: snippet });

    // Was liked, now not
    if (previousLikeState) {
      this.snippetActionsClient
        .unvoteSnippetPositive(id)
        .then(() => this.loadVotes());
    } else {
      this.snippetActionsClient
        .voteSnippetPositive(id)
        .then(() => this.loadVotes());
    }

    // Prevent parent Link to navigate
    e.preventDefault();
  }

  onSnippetDislike(e, id) {
    if (this.enforceUserLogged()) return;

    let previousLikeState = false;
    // Copy snippets array
    let snippet = { ...this.state.snippet };
    // Store previous fav state
    previousLikeState = snippet.userVotedNegative;
    // Update variable
    snippet.userVotedPositive = false;
    snippet.userVotedNegative = !snippet.userVotedNegative;
    // Update state
    this.setState({ snippet: snippet });

    // Was disliked, now not
    if (previousLikeState) {
      this.snippetActionsClient
        .unvoteSnippetNegative(id)
        .then(() => this.loadVotes());
    } else {
      this.snippetActionsClient
        .voteSnippetNegative(id)
        .then(() => this.loadVotes());
    }

    // Prevent parent Link to navigate
    e.preventDefault();
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
          handleLike={this.onSnippetLike}
          handleDislike={this.onSnippetDislike}
        />
      </div>
    );
  }
}

export default SnippetOverview;
