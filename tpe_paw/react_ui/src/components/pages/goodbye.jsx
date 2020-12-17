import React from "react";
import { Link } from "react-router-dom";
import { mdiCodeTags } from "@mdi/js";
import i18n from "../../i18n";
import Icon from "@mdi/react";
import { Helmet } from "react-helmet";

const Goodbye = () => {
  return (
    <div className="form-signin rounded-border">
      <Helmet>
        <title>
          {i18n.t("app")} | {i18n.t("nav.goodbye")}
        </title>
      </Helmet>
      <span
        className="mx-auto text-white login-title"
        style={{ display: "flex", justifyContent: "center" }}
      >
        <Icon path={mdiCodeTags} size={2} />
        <span className="ml-2">{i18n.t("goodbye.title")}</span>
      </span>

      <div className="m-4 p-5 inner-square shadow rounded-border ">
        <Link
          to="/"
          className="btn btn-lg btn-primary btn-block mt-3 rounded-border form-button"
        >
          {i18n.t("goodbye.home")}
        </Link>
        <Link
          to="/login"
          className="btn btn-lg btn-primary btn-block mt-3 rounded-border form-button"
        >
          {i18n.t("goodbye.login")}
        </Link>
        <Link
          to="/signup"
          className="btn btn-lg btn-primary btn-block mt-3 rounded-border form-button"
        >
          {i18n.t("goodbye.create")}
        </Link>
      </div>
    </div>
  );
};

export default Goodbye;
