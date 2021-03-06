import React from "react";
import { Link } from "react-router-dom";
import i18n from "../../../i18n";
import { Helmet } from "react-helmet";

const Page404 = () => {
  return (
    <React.Fragment>
      <Helmet>
        <title>
          {i18n.t("app")} | {i18n.t("errors.e404.header")}
        </title>
      </Helmet>
      <div className="col align-items-horizontal-center flex-col flex-center mt-5">
        <div className="row error-number align-items-horizontal-center fw-700 mb-2">
          {i18n.t("errors.e404.number")}
        </div>
        <div className="row error-title align-items-horizontal-center fw-300 mt-1">
          <em>{i18n.t("errors.e404.title")}</em>
        </div>
        <div className="action-call-box p-3 mt-3">
          <div className="action-call-message m-2">
            {i18n.t("errors.e404.message")}
          </div>
          <Link
            to="/"
            className="btn btn-lg btn-primary mt-3 rounded-border form-button"
          >
            {i18n.t("errors.e404.action")}
          </Link>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Page404;
