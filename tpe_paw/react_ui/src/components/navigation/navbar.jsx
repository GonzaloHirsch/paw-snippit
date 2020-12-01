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
  mdiFilterVariant,
  mdiSortAlphabeticalVariant,
  mdiPlus,
  mdiPlusCircleOutline,
} from "@mdi/js";
import { logOut } from "../../redux/actions/actionCreators";
import { LOGIN_SUCCESS } from "../../redux/actions/actionTypes";

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
      type: "all",
      sort: "asc",
    },
  };

  constructor(props) {
    super(props);
    this.handleLogOut = this.handleLogOut.bind(this);
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

  componentDidMount() {
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
  }

  // We unsubscribe after the component will be unmounted
  componentWillUnmount() {
    this.authUnsubscribe();
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
    // Dispatch the login event
    store.dispatch(logOut());

    // Push to home
    this.props.history.push("/");
  }

  handleSearch(search) {
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
    }
    if (search.type !== null && search.type !== undefined) {
      params.set("type", search.type);
    }
    if (search.sort !== null && search.sort !== undefined) {
      params.set("sort", search.sort);
    }

    // Pushing the route
    this.props.history.push({
      pathname: route,
      search: "?" + params.toString(),
    });
  }

  getTopRightNavItems() {
    if (this.state.userIsLogged) {
      return (
        <div className="col-4">
          <Link to="/create" className="mx-1 text-white">
            <Icon path={mdiPlusCircleOutline} size={1} />
          </Link>
          <Link to="/profile" className="mx-1 text-white">
            <em className="ml-1">
              {i18n.t("nav.greeting", { user: store.getState().auth.info.sub })}
            </em>
          </Link>
        </div>
      );
    } else {
      return (
        <div className="col-4">
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
        </div>
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
        />
        <nav className="navbar navbar-expand-lg navbar-dark fixed-top row row-cols-3">
          <button
            className="btn btn-sm text-white col-1"
            type="button"
            data-toggle="collapse"
            aria-expanded="false"
            aria-label="Toggle side navigation"
            onClick={() => this.navInteract(this.state.navIsOpen)}
          >
            {this.getNavIcon(this.state.navIsOpen)}
          </button>
          <Link to="/" className="app-link text-white col-2">
            <Icon path={mdiCodeTags} size={2} />
            <span className="ml-1">{i18n.t("app")}</span>
          </Link>
          <button
            className="navbar-toggler"
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
            className="collapse navbar-collapse col-9"
            id="navbarSupportedContent"
          >
            <form
              className="form-inline my-auto my-lg-0 col-8"
              onSubmit={() => this.handleSearch(this.state.search)}
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
                    <option defaultValue value="all">
                      Choose...
                    </option>
                    <option value="all">all</option>
                    <option value="tag">tag</option>
                    <option value="title">title</option>
                    <option value="content">content</option>
                    <option value="username">username</option>
                    <option value="language">language</option>
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
                    <option defaultValue value="asc">
                      Choose...
                    </option>
                    <option value="asc">asc</option>
                    <option value="desc">desc</option>
                    <option value="no">no</option>
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
            {this.getTopRightNavItems()}
          </div>
        </nav>
      </div>
    );
  }
}

export default withRouter(NavBar);
