import React, { Component } from "react";
import Icon from "@mdi/react";

export const SidenavButtonItem = ({ onEvent, icon, text }) => {
  return (
    <a
      onClick={onEvent}
      className="align-items-vertical"
      style={{ cursor: "pointer" }}
    >
      <Icon path={icon} size={1} />
      <span className="ml-2">{text}</span>
    </a>
  );
};
