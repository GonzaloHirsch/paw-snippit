import React from "react";
import { Link } from "react-router-dom";

const LinkDetailBox = (props) => {
  return (
    <Link
      to={props.path}
      className="boxed-content-link shadow p-3 m-2 flex-col align-items-vertical"
      style={{ minWidth: "190px" }}
    >
      {props.children}
    </Link>
  );
};

export default LinkDetailBox;
