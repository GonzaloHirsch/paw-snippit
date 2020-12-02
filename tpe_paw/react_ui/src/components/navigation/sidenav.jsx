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
import { CONTEXT } from "../../js/constants";

class Sidenav extends Component {
  state = {};

  getSidenavRoleItems(roles, currentContext, onNavigationChange) {
    return (
      <React.Fragment>
        <div className="dropdown-divider menu-divider"></div>
        <ul className="menu-list">
          {!isAdmin(roles) ? (
            <SidenavLinkItem
              icon={mdiAccount}
              route="/profile"
              context={CONTEXT.PROFILE}
              currentContext={currentContext}
              text={i18n.t("nav.profile")}
              onNavigationChange={onNavigationChange}
            />
          ) : (
            <SidenavLinkItem
              icon={mdiFlag}
              route="/flagged"
              context={CONTEXT.FLAGGED}
              currentContext={currentContext}
              text={i18n.t("nav.flagged")}
              onNavigationChange={onNavigationChange}
            />
          )}
          <SidenavLinkItem
            icon={mdiTagHeart}
            route="/following"
            context={CONTEXT.FOLLOWING}
            currentContext={currentContext}
            text={i18n.t("nav.following")}
            onNavigationChange={onNavigationChange}
          />
          <SidenavLinkItem
            icon={mdiHeart}
            route="/favorites"
            context={CONTEXT.FAVORITES}
            currentContext={currentContext}
            text={i18n.t("nav.favorites")}
            onNavigationChange={onNavigationChange}
          />
          <SidenavLinkItem
            icon={mdiThumbUp}
            route="/upvoted"
            context={CONTEXT.UPVOTED}
            currentContext={currentContext}
            text={i18n.t("nav.upvoted")}
            onNavigationChange={onNavigationChange}
          />
        </ul>
      </React.Fragment>
    );
  }

  render() {
    const { isLogged, roles, onLogOut, currentContext, onNavigationChange } = this.props;
    return (
      <div id="mySidenav" className="sidenav">
        <ul className="menu-list">
          <SidenavLinkItem
            icon={mdiHome}
            route="/"
            context={CONTEXT.HOME}
            currentContext={currentContext}
            text={i18n.t("nav.home")}
            onNavigationChange={onNavigationChange}
          />
          <SidenavLinkItem
            icon={mdiTag}
            route="/tags"
            context={CONTEXT.TAGS}
            currentContext={currentContext}
            text={i18n.t("nav.tags")}
            onNavigationChange={onNavigationChange}
          />
          <SidenavLinkItem
            icon={mdiMonitor}
            route="/languages"
            context={CONTEXT.LANGUAGES}
            currentContext={currentContext}
            text={i18n.t("nav.languages")}
            onNavigationChange={onNavigationChange}
          />
          <SidenavLinkItem
            icon={mdiMagnify}
            route="/explore"
            context={CONTEXT.EXPLORE}
            currentContext={currentContext}
            text={i18n.t("nav.explore")}
            onNavigationChange={onNavigationChange}
          />
        </ul>
        {isLogged && this.getSidenavRoleItems(roles, currentContext, onNavigationChange)}
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
