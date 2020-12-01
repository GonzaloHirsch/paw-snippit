import React, { Component } from "react";
import {
  mdiHome,
  mdiMagnify,
  mdiTag,
  mdiMonitor,
  mdiTagHeart,
  mdiHeart,
  mdiFlag,
  mdiAccount,
  mdiPower,
  mdiThumbUp,
} from "@mdi/js";
import i18n from "../../i18n";
import { SidenavLinkItem } from "./sidenav_link_item";
import { SidenavButtonItem } from "./sidenav_button_item";
import { isAdmin } from "../../js/security_utils";

class Sidenav extends Component {
  state = {};

  getSidenavRoleItems(roles) {
    return (
      <React.Fragment>
        <div className="dropdown-divider menu-divider"></div>
        <ul className="menu-list">
          {!isAdmin(roles) ? (
            <SidenavLinkItem
              icon={mdiAccount}
              route="/profile"
              text={i18n.t("nav.profile")}
            />
          ) : (
            <SidenavLinkItem
              icon={mdiFlag}
              route="/flagged"
              text={i18n.t("nav.flagged")}
            />
          )}
          <SidenavLinkItem
            icon={mdiTagHeart}
            route="/following"
            text={i18n.t("nav.following")}
          />
          <SidenavLinkItem
            icon={mdiHeart}
            route="/favorites"
            text={i18n.t("nav.favorites")}
          />
          <SidenavLinkItem
            icon={mdiThumbUp}
            route="/upvoted"
            text={i18n.t("nav.upvoted")}
          />
        </ul>
      </React.Fragment>
    );
  }

  render() {
    const { isLogged, roles, onLogOut } = this.props;
    return (
      <div id="mySidenav" className="sidenav">
        <ul className="menu-list">
          <SidenavLinkItem icon={mdiHome} route="/" text={i18n.t("nav.home")} />
          <SidenavLinkItem
            icon={mdiTag}
            route="/tags"
            text={i18n.t("nav.tags")}
          />
          <SidenavLinkItem
            icon={mdiMonitor}
            route="/languages"
            text={i18n.t("nav.languages")}
          />
          <SidenavLinkItem
            icon={mdiMagnify}
            route="/explore"
            text={i18n.t("nav.explore")}
          />
        </ul>
        {isLogged && this.getSidenavRoleItems(roles)}
        {isLogged && (
          <React.Fragment>
            <div className="dropdown-divider menu-divider"></div>
            <ul className="menu-list">
              <SidenavButtonItem
                icon={mdiPower}
                onEvent={onLogOut}
                text={i18n.t("nav.logout")}
              />
            </ul>
          </React.Fragment>
        )}
      </div>
    );
  }
}

export default Sidenav;
