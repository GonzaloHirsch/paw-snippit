import React, { Component } from "react";
import Icon from "@mdi/react";
import { Link } from "react-router-dom";

export const SidenavLinkItem = ({
  route,
  icon,
  text,
  context,
  currentContext,
  onNavigationChange,
}) => {
  return (
    <Link
      to={route}
      className={
        "align-items-vertical sidenav-link-item " +
        ((context === currentContext || "") && "selected")
      }
      onClick={() => onNavigationChange(context)}
    >
      <Icon path={icon} size={1} />
      <span className="ml-2">{text}</span>
    </Link>
  );
};
