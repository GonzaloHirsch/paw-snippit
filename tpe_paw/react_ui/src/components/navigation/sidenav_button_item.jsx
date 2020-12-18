import React from "react";
import Icon from "@mdi/react";

export const SidenavButtonItem = ({ onEvent, icon, text }) => {
  return (
    <div
      onClick={onEvent}
      className="align-items-vertical sidenav-link-item sidenav-button-item"
      style={{ cursor: "pointer" }}
    >
      <Icon path={icon} size={1} />
      <span className="ml-2">{text}</span>
    </div>
  );
};
