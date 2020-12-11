import React from "react";
import { Link } from "react-router-dom";
import i18n from "../../../i18n";

const Page500 = () => {
  return (
    <React.Fragment>
      <div className="col align-items-horizontal-center flex-col flex-center mt-5">
        <div className="row error-number align-items-horizontal-center fw-700 mb-2">
          {i18n.t("errors.e500.number")}
        </div>
        <div className="row error-title align-items-horizontal-center fw-300 mt-1">
          <em>{i18n.t("errors.e500.title")}</em>
        </div>
        <div className="action-call-box p-3 mt-3">
          <div className="action-call-message m-2">
            {i18n.t("errors.e500.message")}
          </div>
          <Link
            to="/"
            className="btn btn-lg btn-primary mt-3 rounded-border form-button"
          >
            {i18n.t("errors.e500.action")}
          </Link>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Page500;
