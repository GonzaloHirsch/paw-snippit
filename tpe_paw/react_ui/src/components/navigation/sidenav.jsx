import React, { Component } from "react";
import { mdiHome, mdiMagnify, mdiTag, mdiMonitor } from "@mdi/js";
import i18n from "../../i18n";
import { SidenavItem } from "./sidenav_item";

class Sidenav extends Component {
  state = {};

  render() {
    const isLogged = this.props.isLogged;
    return (
      <div id="mySidenav" className="sidenav">
        <ul className="menu-list">
          <SidenavItem icon={mdiHome} route="/" text={i18n.t("nav.home")} />
          <SidenavItem
            icon={mdiMonitor}
            route="/tags"
            text={i18n.t("nav.tags")}
          />
          <SidenavItem
            icon={mdiHome}
            route="/languages"
            text={i18n.t("nav.languages")}
          />
          <SidenavItem
            icon={mdiMagnify}
            route="/explore"
            text={i18n.t("nav.explore")}
          />
        </ul>
        {isLogged && (
            <h1>HEY</h1>
        )}
      </div>
    );
  }
}

export default Sidenav;
