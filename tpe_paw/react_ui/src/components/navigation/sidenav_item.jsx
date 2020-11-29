import React, { Component } from "react";
import Icon from "@mdi/react";
import { Link } from "react-router-dom";

export const SidenavItem = ({ route, icon, text }) => {
  return (
    <Link to={route} className="align-items-vertical">
      <Icon path={icon} size={1} />
      <span className="ml-2">{text}</span>
    </Link>
  );
};
