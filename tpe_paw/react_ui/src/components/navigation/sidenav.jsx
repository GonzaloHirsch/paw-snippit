import React, { Component } from "react";
import { mdiHome, mdiMagnify, mdiTag, mdiMonitor } from "@mdi/js";
import i18n from "../../i18n";
import {SidenavItem} from "./sidenav_item";

class Sidenav extends Component {
  state = {};
  render() {
    return (
      <div id="mySidenav" className="sidenav">
        <ul className="menu-list">
          <SidenavItem icon={mdiHome} path="/" text={i18n.t("nav.home")} />
          <SidenavItem icon={mdiMonitor} path="/tags" text={i18n.t("nav.tags")} />
          <SidenavItem icon={mdiHome} path="/languages" text={i18n.t("nav.languages")} />
          <SidenavItem icon={mdiMagnify} path="/explore" text={i18n.t("nav.explore")} />
        </ul>
      </div>
    );
  }
}

export default Sidenav;
