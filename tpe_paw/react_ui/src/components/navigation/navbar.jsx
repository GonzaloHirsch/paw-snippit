import React, { Component } from "react";
import Sidenav from "./sidenav";
import store from "../../store";
import { Link, withRouter, matchPath } from "react-router-dom";
import i18n from "../../i18n";
import Icon from "@mdi/react";
import {
  mdiClose,
  mdiMenu,
  mdiCodeTags,
  mdiMagnify,
  mdiPlusCircleOutline,
} from "@mdi/js";
import { logOut } from "../../redux/actions/actionCreators";
import { LOGIN_SUCCESS } from "../../redux/actions/actionTypes";
import { CONTEXT } from "../../js/constants";

// Component -> https://getbootstrap.com/docs/4.5/components/navbar/
class NavBar extends Component {
  authUnsubscribe;
  state = {
    navIsOpen: false,
    userIsLogged: false,
    username: "",
    roles: [],
    search: {
      query: "",
      type: "0",
      sort: "0",
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

  testForContext(test, ctx) {
    const isCtx = !!matchPath(this.props.location.pathname, test);
    if (isCtx && this.state.currentContext !== ctx) {
      this.setState({ currentContext: ctx });
    }
    return isCtx;
  }

  determineCurrentContext() {
    if (this.testForContext("**/tags", CONTEXT.TAGS)) return;
    if (this.testForContext("**/languages", CONTEXT.LANGUAGES)) return;
    if (this.testForContext("**/explore", CONTEXT.EXPLORE)) return;
    if (this.testForContext("**/profile", CONTEXT.PROFILE)) return;
    if (this.testForContext("**/following", CONTEXT.FOLLOWING)) return;
    if (this.testForContext("**/favorites", CONTEXT.FAVORITES)) return;
    if (this.testForContext("**/upvoted", CONTEXT.UPVOTED)) return;
    if (this.testForContext("**/flagged", CONTEXT.FLAGGED)) return;
    if (this.testForContext("**/", CONTEXT.HOME)) return;
  }

  componentDidMount() {
    console.log(this.props, "PROPS");
    // Subscribing to changes in the auth status, if the user logs in, it is marked as logged in
    this.authUnsubscribe = store.subscribe(() => {
      const storedState = store.getState();
      this.handleReduxUpdate(storedState);
    });

    // Initial state loading in case it comes from localstorage
    const storedState = store.getState();
    this.handleReduxUpdate(storedState);

    const isSearching = !!matchPath(this.props.location.pathname, "**/search");
    if (isSearching) {
      const params = new URLSearchParams(this.props.location.search);
      this.setState({
        search: {
          query: params.get("query"),
          type: params.get("type"),
          sort: params.get("sort"),
        },
      });
    }

    this.determineCurrentContext();
  }

  // We unsubscribe after the component will be unmounted
  componentWillUnmount() {
    this.authUnsubscribe();
  }

  componentDidUpdate() {
    this.determineCurrentContext();
  }

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

  handleSearch(event, search) {
    event.preventDefault();
    // Determine if we add the "search" to the route
    const isSearching = !!matchPath(this.props.location.pathname, "**/search");
    let route;
    if (isSearching) {
      route = this.props.location.pathname;
    } else {
      let toAdd = "search";
      if (
        !(
          this.props.location.pathname.charAt(
            this.props.location.pathname.length - 1
          ) === "/"
        )
      ) {
        toAdd = "/search";
      }
      route = this.props.location.pathname + toAdd;
    }

    // Adding the params to not lose the existing ones
    let params = new URLSearchParams(this.props.location.search);
    if (search.query !== null && search.query !== undefined) {
      params.set("query", search.query);
    } else {
      params.set("query", "");
    }
    if (
      search.type !== null &&
      search.type !== undefined &&
      search.type !== "0"
    ) {
      params.set("type", search.type);
    } else {
      params.set("type", "all");
    }
    if (
      search.sort !== null &&
      search.sort !== undefined &&
      search.sort !== "0"
    ) {
      params.set("sort", search.sort);
    } else {
      params.set("sort", "no");
    }

    this.setState({ search: search });

    // Pushing the route
    this.props.history.push({
      pathname: route,
      search: "?" + params.toString(),
    });
  }

  getTopRightNavItems() {
    if (this.state.userIsLogged) {
      return (
        <React.Fragment>
          <Link to="/create" className="mx-1 text-white">
            <Icon path={mdiPlusCircleOutline} size={1} />
          </Link>
          <Link to="/profile" className="mx-1 text-white">
            <em className="ml-1">
              {i18n.t("nav.greeting", { user: store.getState().auth.info.sub })}
            </em>
          </Link>
        </React.Fragment>
      );
    } else {
      return (
        <React.Fragment>
          <Link to="/login" className="mx-1">
            <button type="button" className="btn btn-light">
              {i18n.t("nav.login")}
            </button>
          </Link>
          <Link to="/signup" className="mx-1">
            <button type="button" className="btn btn-light">
              {i18n.t("nav.signup")}
            </button>
          </Link>
        </React.Fragment>
      );
    }
  }

  handleNavigationChange(newCtx) {
    // Closing the sidebar
    this.navInteract(true);
    this.setState({ currentContext: newCtx });
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
        <nav className="navbar navbar-expand-lg navbar-dark fixed-top row row-cols-3">
          <button
            className="btn btn-sm text-white col-2 col-sm-1 no-focus"
            type="button"
            aria-label="Toggle side navigation"
            onClick={() => this.navInteract(this.state.navIsOpen)}
          >
            {this.getNavIcon(this.state.navIsOpen)}
          </button>
          <Link
            to="/"
            className="app-link text-white col-7 col-lg-2 align-items-horizontal-center"
            onClick={() => this.handleNavigationChange(CONTEXT.HOME)}
          >
            <Icon path={mdiCodeTags} size={2}></Icon>
            <span className="ml-1">{i18n.t("app")}</span>
          </Link>
          <button
            className="navbar-toggler btn btn-sm text-white col-2 col-sm-1 no-focus"
            type="button"
            data-toggle="collapse"
            data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div
            className="collapse navbar-collapse col-2 col-lg-9"
            id="navbarSupportedContent"
          >
            <form
              className="form-inline my-auto my-lg-0 col-8"
              onSubmit={(e) => this.handleSearch(e, this.state.search)}
            >
              <div className="input-group mr-sm-2 search-box">
                <input
                  type="text"
                  className="form-control"
                  placeholder={i18n.t("nav.searchHint")}
                  aria-label={i18n.t("nav.searchHint")}
                  aria-describedby="button-addon2"
                  onChange={(e) =>
                    this.setState({
                      search: {
                        query: e.target.value,
                        type: this.state.search.type,
                        sort: this.state.search.sort,
                      },
                    })
                  }
                  value={this.state.search.query}
                />
                <div className="input-group-append">
                  <select
                    className="custom-select form-control"
                    id="inputGroupSelect02"
                    onChange={(e) =>
                      this.setState({
                        search: {
                          query: this.state.search.query,
                          type: e.target.value,
                          sort: this.state.search.sort,
                        },
                      })
                    }
                    value={this.state.search.type}
                  >
                    <option value="0">{i18n.t("nav.filter.hint")}</option>
                    <option value="all">{i18n.t("nav.filter.all")}</option>
                    <option value="tag">{i18n.t("nav.filter.tag")}</option>
                    <option value="title">{i18n.t("nav.filter.title")}</option>
                    <option value="content">
                      {i18n.t("nav.filter.content")}
                    </option>
                    <option value="username">
                      {i18n.t("nav.filter.username")}
                    </option>
                    <option value="language">
                      {i18n.t("nav.filter.language")}
                    </option>
                  </select>
                  <select
                    className="custom-select form-control"
                    id="inputGroupSelect03"
                    onChange={(e) =>
                      this.setState({
                        search: {
                          query: this.state.search.query,
                          type: this.state.search.type,
                          sort: e.target.value,
                        },
                      })
                    }
                    value={this.state.search.sort}
                  >
                    <option value="0">{i18n.t("nav.order.hint")}</option>
                    <option value="asc">{i18n.t("nav.order.ascending")}</option>
                    <option value="desc">
                      {i18n.t("nav.order.descending")}
                    </option>
                    <option value="no">{i18n.t("nav.order.no")}</option>
                  </select>
                  <button
                    className="btn btn-outline-secondary"
                    type="submit"
                    id="button-addon2"
                  >
                    <Icon path={mdiMagnify} size={1} />
                  </button>
                </div>
              </div>
            </form>
            <div className="nav-item col-4 align-items-horizontal-right">
              {this.getTopRightNavItems()}
            </div>
          </div>
        </nav>
      </div>
    );
  }
}

export default withRouter(NavBar);
