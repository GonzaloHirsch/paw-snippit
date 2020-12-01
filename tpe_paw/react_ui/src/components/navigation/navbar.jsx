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
      filter: "",
      order: "",
    },
  };

  constructor(props) {
    super(props);
    this.handleLogOut = this.handleLogOut.bind(this);
  }

  componentDidMount() {
    // Subscribing to changes in the auth status, if the user logs in, it is marked as logged in
    this.authUnsubscribe = store.subscribe(() => {
      const storedState = store.getState();
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
    });

    const isSearching = !!matchPath(this.props.location.pathname, "**/search");
    if (isSearching) {
      const params = new URLSearchParams(this.props.location.search);
      this.setState({
        search: {
          query: params.get("query"),
          filter: params.get("filter"),
          order: params.get("order"),
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
      route = this.props.location.pathname + "search";
    }

    // Adding the params to not lose the existing ones
    let params = new URLSearchParams(this.props.location.search);
    if (search.query !== null && search.query !== undefined) {
      params.set("query", search.query);
    }
    if (search.filter !== null && search.filter !== undefined) {
      params.set("filter", search.filter);
    }
    if (search.order !== null && search.order !== undefined) {
      params.set("order", search.order);
    }

    // Pushing the route
    this.props.history.push({
      pathname: route,
      search: "?" + params.toString(),
    });
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
                        filter: this.state.search.filter,
                        order: this.state.search.order,
                      },
                    })
                  }
                  value={this.state.search.query}
                />
                <div className="input-group-append">
                  <button
                    className="btn btn-outline-secondary"
                    type="submit"
                    id="button-addon2"
                  >
                    <Icon path={mdiMagnify} size={1} />
                  </button>
                </div>
              </div>

              <div className="input-group mb-3">
                <select
                  className="custom-select form-control"
                  id="inputGroupSelect02"
                  onChange={(e) =>
                    this.setState({
                      search: {
                        query: this.state.search.query,
                        filter: e.target.value,
                        order: this.state.search.order,
                      },
                    })
                  }
                  value={this.state.search.filter}
                >
                  <option defaultValue>Choose...</option>
                  <option value="1">One</option>
                  <option value="2">Two</option>
                  <option value="3">Three</option>
                </select>
                <div className="input-group-append">
                  <span
                    className="input-group-text"
                    htmlFor="inputGroupSelect02"
                  >
                    <Icon path={mdiFilterVariant} size={1} />
                  </span>
                </div>
              </div>

              <div className="input-group mb-3">
                <select
                  className="custom-select form-control"
                  id="inputGroupSelect03"
                  onChange={(e) =>
                    this.setState({
                      search: {
                        query: this.state.search.query,
                        filter: this.state.search.filter,
                        order: e.target.value,
                      },
                    })
                  }
                  value={this.state.search.order}
                >
                  <option defaultValue>Choose...</option>
                  <option value="1">One</option>
                  <option value="2">Two</option>
                  <option value="3">Three</option>
                </select>
                <div className="input-group-append">
                  <span
                    className="input-group-text"
                    htmlFor="inputGroupSelect03"
                  >
                    <Icon path={mdiSortAlphabeticalVariant} size={1} />
                  </span>
                </div>
              </div>
              {/* 
              <input
                className="form-control mr-sm-2"
                type="search"
                placeholder="Search"
                aria-label="Search"
              />
              <button
                className="btn btn-outline-success my-2 my-sm-0"
                type="submit"
              >
                Search
              </button> */}
            </form>
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
          </div>
        </nav>
      </div>
    );
  }
}

export default withRouter(NavBar);
