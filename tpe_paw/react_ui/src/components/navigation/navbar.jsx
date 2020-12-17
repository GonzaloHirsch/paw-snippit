// Components
import React, { Component } from "react";
import Sidenav from "./sidenav";
import { Link, withRouter, matchPath } from "react-router-dom";
import Icon from "@mdi/react";
import SearchBar from "./searchbar";

// Redux
import store from "../../store";
import { logOut } from "../../redux/actions/actionCreators";
import { LOGIN_SUCCESS } from "../../redux/actions/actionTypes";

// Libs
import i18n from "../../i18n";
import { mdiClose, mdiMenu, mdiCodeTags, mdiAccount } from "@mdi/js";
import { CONTEXT, CONTEXT_WITHOUT_SEARCH } from "../../js/constants";
import {
  getNavSearchFromUrl,
  getTagsSearchFromUrl,
  getLanguagesSearchFromUrl,
  fillNavSearchFromUrl,
  fillTagSearchFromUrl,
  fillLanguageSearchFromUrl,
  fillDefaultNavSearchFromUrl,
  fillDefaultTagSearchFromUrl,
  fillDefaultLanguageSearchFromUrl,
} from "../../js/search_from_url";
import { areEqualShallow } from "../../js/comparison";
import { isAdmin } from "../../js/security_utils";

// Component -> https://getbootstrap.com/docs/4.5/components/navbar/
class NavBar extends Component {
  authUnsubscribe;
  userClient;

  state = {
    navIsOpen: false,
    userIsLogged: false,
    showSearch: true,
    username: "",
    roles: [],
    search: {
      query: "",
      type: "0",
      sort: "0",
      showEmpty: true,
      showOnlyFollowing: false,
    },
    currentContext: CONTEXT.HOME,
  };

  constructor(props) {
    super(props);
    this.handleLogOut = this.handleLogOut.bind(this);
    this.handleNavigationChange = this.handleNavigationChange.bind(this);
  }

  handleReduxUpdate(storedState) {
    if (storedState.auth.status === LOGIN_SUCCESS) {
      this.setState({
        userIsLogged: true,
        username: storedState.auth.info.sub,
        roles: storedState.auth.roles,
      });
    } else {
      this.setState({
        userIsLogged: false,
        username: "",
        roles: [],
      });
    }
  }

  // Determining context
  // --------------------------------------------------------------------

  testForContext(test, ctx) {
    const isCtx = !!matchPath(this.props.location.pathname, test);
    if (isCtx) {
      // Determine if show search
      let showSearch = true;
      if (CONTEXT_WITHOUT_SEARCH.includes(ctx)) {
        showSearch = false;
      }
      // Get the initial search value
      let initialSearch = this.getSearchBasedOnContext(ctx);
      // Fill with default values in case some not found
      initialSearch = this.fillDefaultSearchBasedOnContext(initialSearch, ctx);
      if (this.state.currentContext !== ctx) {
        this.setState({
          currentContext: ctx,
          showSearch: showSearch,
          search: initialSearch,
        });
      } else if (this.state.showSearch !== showSearch) {
        this.setState({ showSearch: showSearch });
      } else if (!areEqualShallow(initialSearch, this.state.search)) {
        this.setState({ search: initialSearch });
      }
    }
    return isCtx;
  }

  determineCurrentContext() {
    if (this.testForContext("**/tags/search", CONTEXT.TAGS)) return;
    if (this.testForContext("**/languages/search", CONTEXT.LANGUAGES)) return;
    if (this.testForContext("**/tags/*", CONTEXT.TAGS_SNIPPETS)) return;
    if (this.testForContext("**/languages/*", CONTEXT.LANGUAGES_SNIPPETS))
      return;
    if (this.testForContext("**/tags", CONTEXT.TAGS)) return;
    if (this.testForContext("**/languages", CONTEXT.LANGUAGES)) return;
    if (this.testForContext("**/explore", CONTEXT.EXPLORE)) return;
    if (this.testForContext("**/profile", CONTEXT.PROFILE)) return;
    if (this.testForContext("**/following", CONTEXT.FOLLOWING)) return;
    if (this.testForContext("**/favorites", CONTEXT.FAVORITES)) return;
    if (this.testForContext("**/upvoted", CONTEXT.UPVOTED)) return;
    if (this.testForContext("**/flagged", CONTEXT.FLAGGED)) return;
    if (this.testForContext("**/404", CONTEXT.ERROR)) return;
    if (this.testForContext("**/500", CONTEXT.ERROR)) return;
    if (this.testForContext("**/login", CONTEXT.LOGIN)) return;
    if (this.testForContext("**/signup", CONTEXT.SIGNUP)) return;
    if (this.testForContext("**/recover", CONTEXT.RECOVER)) return;
    if (this.testForContext("**/verify", CONTEXT.VERIFY)) return;
    if (this.testForContext("**/reset-password", CONTEXT.RESET_PASSWORD))
      return;
    if (this.testForContext("**/goodbye", CONTEXT.GOODBYE)) return;
    if (this.testForContext("**/snippets/create", CONTEXT.SNIPPET_CREATE))
      return;
    if (this.testForContext("**/items/create", CONTEXT.ITEMS_CREATE)) return;
    if (this.testForContext("**/", CONTEXT.HOME)) return;
  }

  // Search methods
  // --------------------------------------------------------------------

  // Recovers the search params from the url
  getSearchBasedOnContext(ctx) {
    const urlSearch = this.props.location.search;
    if (ctx === CONTEXT.TAGS) {
      return getTagsSearchFromUrl(urlSearch);
    } else if (ctx === CONTEXT.LANGUAGES) {
      return getLanguagesSearchFromUrl(urlSearch);
    }
    return getNavSearchFromUrl(urlSearch);
  }

  // Adds possible missing params from the url
  fillSearchBasedOnContext(search, ctx) {
    let params = new URLSearchParams(this.props.location.search);
    if (ctx === CONTEXT.TAGS) {
      return fillTagSearchFromUrl(search, params);
    } else if (ctx === CONTEXT.LANGUAGES) {
      return fillLanguageSearchFromUrl(search, params);
    }
    return fillNavSearchFromUrl(search, params);
  }

  // Fills or replaces search params in case inexistent or wrong
  fillDefaultSearchBasedOnContext(search, ctx) {
    if (ctx === CONTEXT.TAGS) {
      return fillDefaultTagSearchFromUrl(search);
    } else if (ctx === CONTEXT.LANGUAGES) {
      return fillDefaultLanguageSearchFromUrl(search);
    }
    return fillDefaultNavSearchFromUrl(search);
  }

  // Lifecycle hooks
  // --------------------------------------------------------------------

  componentDidMount() {
    // Subscribing to changes in the auth status, if the user logs in, it is marked as logged in
    this.authUnsubscribe = store.subscribe(() => {
      const storedState = store.getState();
      this.handleReduxUpdate(storedState);
    });

    // Initial state loading in case it comes from localstorage
    const storedState = store.getState();
    this.handleReduxUpdate(storedState);

    // Determine current context
    this.determineCurrentContext();

    const isSearching = !!matchPath(this.props.location.pathname, "**/search");
    if (isSearching) {
      const search = this.getSearchBasedOnContext(this.state.currentContext);
      this.setState({ search: search });
    }
  }

  // We unsubscribe after the component will be unmounted
  componentWillUnmount() {
    this.authUnsubscribe();
  }

  componentDidUpdate() {
    this.determineCurrentContext();
  }

  // Events
  // --------------------------------------------------------------------

  // Change status of the nav variable
  navInteract = (isNavOpen) => {
    if (isNavOpen === true) {
      document.getElementById("mySidenav").style.width = "0";
    } else {
      document.getElementById("mySidenav").style.width = "200px";
    }
    this.setState({ navIsOpen: !isNavOpen });
  };

  // Method to get the right navigation icon
  getNavIcon = (isNavOpen) => {
    if (isNavOpen === true) {
      return <Icon path={mdiClose} size={1} />;
    } else {
      return <Icon path={mdiMenu} size={1} />;
    }
  };

  // Method to handle the log out event
  handleLogOut() {
    // Closing the sidebar
    this.navInteract(true);

    // Dispatch the login event
    store.dispatch(logOut());

    // Push to home
    this.props.history.push("/goodbye");
  }

  handleNavigationChange(newCtx) {
    // Closing the sidebar
    this.navInteract(true);
    this.setState({ currentContext: newCtx });
  }

  // Component getters
  // --------------------------------------------------------------------

  getTopRightNavItems() {
    if (this.state.userIsLogged) {
      return (
        <React.Fragment>
          <Link
            to={
              isAdmin(this.state.roles) ? "/items/create" : "/snippets/create"
            }
            className="create-button shadow btn rounded-border mr-2 ml-2 "
          >
            {i18n.t("create")}
          </Link>
          {!isAdmin(this.state.roles) && (
            <Link to="/profile" className="mx-1 text-white">
              <Icon
                path={mdiAccount}
                className="top-profile-image"
                size={2}
              ></Icon>
            </Link>
          )}
        </React.Fragment>
      );
    } else {
      return (
        <React.Fragment>
          <Link
            to={{
              pathname: "/login",
              state: { from: this.props.history.location },
            }}
            className="mx-1"
          >
            <button type="button" className="btn btn-light">
              {i18n.t("nav.login")}
            </button>
          </Link>
          <Link
            to={{
              pathname: "/signup",
              state: { from: this.props.history.location },
            }}
            className="mx-1"
          >
            <button type="button" className="btn btn-light">
              {i18n.t("nav.signup")}
            </button>
          </Link>
        </React.Fragment>
      );
    }
  }

  render() {
    return (
      <div className="mb-1 text-white nav-parent">
        <Sidenav
          isLogged={this.state.userIsLogged}
          roles={this.state.roles}
          onLogOut={this.handleLogOut}
          currentContext={this.state.currentContext}
          onNavigationChange={this.handleNavigationChange}
        />
        <nav className="navbar navbar-dark fixed-top d-flex justify-space-between">
          <div className="d-flex flex-row">
            <button
              className="btn btn-sm text-white no-focus"
              type="button"
              aria-label="Toggle side navigation"
              onClick={() => this.navInteract(this.state.navIsOpen)}
            >
              {this.getNavIcon(this.state.navIsOpen)}
            </button>
            <Link
              to="/"
              className={"px-2 app-link text-white flex-center "}
              onClick={() => this.handleNavigationChange(CONTEXT.HOME)}
            >
              <Icon path={mdiCodeTags} size={2}></Icon>
              <span className="ml-1">{i18n.t("app")}</span>
            </Link>
          </div>
          <div
            className={
              "d-flex align-items-center " +
              (this.state.showSearch
                ? "navbar-spacing-responsive justify-space-between"
                : "navbar-spacing justify-end")
            }
          >
            {this.state.showSearch && (
              <SearchBar
                ctx={this.state.currentContext}
                search={this.state.search}
                handleSearch={this.handleSearch}
                handleSearchChange={this.handleSearchChange}
                handleSearchChangeAndSearch={this.handleSearchChangeAndSearch}
              />
            )}
            <div className="nav-item align-items-horizontal-right auth-buttons">
              {this.getTopRightNavItems()}
            </div>
          </div>
        </nav>
      </div>
    );
  }
}

export default withRouter(NavBar);
