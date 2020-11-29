import React, { Component } from "react";

// Routing
// We need to enclose each of the links inside the Link object
import { Link } from "react-router-dom";

// i18n
import i18n from "../../i18n";

// Icons
import Icon from "@mdi/react";
import { mdiClose, mdiMenu, mdiCodeTags, mdiMagnify } from "@mdi/js";

// Component -> https://getbootstrap.com/docs/4.5/components/navbar/
class NavBar extends Component {
  state = {
    navIsOpen: false,
  };

  // Change status of the nav variable
  navInteract = (isNavOpen) => {
    if (isNavOpen === true) {
      document.getElementById("mySidenav").style.width = "0";
    } else {
      document.getElementById("mySidenav").style.width = "180px";
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

  render() {
    return (
      <div className="mb-1 text-white nav-parent">
        <div id="mySidenav" className="sidenav">
          <Link to="/login">
            <span>Login</span>
          </Link>
          <Link to="/login">
            <span>About</span>
          </Link>
        </div>
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
            {/* <ul className="navbar-nav mr-auto">
                <li className="nav-item active">
                  <a className="nav-link" href="#">
                    Home <span className="sr-only">(current)</span>
                  </a>
                </li>
                <li className="nav-item">
                  <a className="nav-link" href="#">
                    Link
                  </a>
                </li>
                <li className="nav-item dropdown">
                  <a
                    className="nav-link dropdown-toggle"
                    href="#"
                    id="navbarDropdown"
                    role="button"
                    data-toggle="dropdown"
                    aria-haspopup="true"
                    aria-expanded="false"
                  >
                    Dropdown
                  </a>
                  <div
                    className="dropdown-menu"
                    aria-labelledby="navbarDropdown"
                  >
                    <a className="dropdown-item" href="#">
                      Action
                    </a>
                    <a className="dropdown-item" href="#">
                      Another action
                    </a>
                    <div className="dropdown-divider"></div>
                    <a className="dropdown-item" href="#">
                      Something else here
                    </a>
                  </div>
                </li>
                <li className="nav-item">
                  <a
                    className="nav-link disabled"
                    href="#"
                    tabIndex="-1"
                    aria-disabled="true"
                  >
                    Disabled
                  </a>
                </li>
              </ul> */}
            <form className="form-inline my-auto my-lg-0 col-8">
              <div className="input-group mr-sm-2 search-box">
                <input
                  type="text"
                  className="form-control"
                  placeholder={i18n.t("nav.searchHint")}
                  aria-label={i18n.t("nav.searchHint")}
                  aria-describedby="button-addon2"
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
              <button type="button" class="btn btn-light">
                {i18n.t("nav.login")}
              </button>
            </Link>
            <Link to="/signup" className="mx-1">
              <button type="button" class="btn btn-light">
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

export default NavBar;
