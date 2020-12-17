import React from "react";
import i18n from "../../i18n";
import { mdiDeleteSweep } from "@mdi/js";
import Icon from "@mdi/react";

// Stateless Functional Component

const SnippetDeletedMessage = () => {
  return (
    <div className="row danger-message p-3 mb-3 align-items-vertical">
      <Icon className="mr-1 icon-danger" path={mdiDeleteSweep} size={3} />
      <div className="col">
        <h3 className="fw-700">{i18n.t("snippetDetail.deleted.title")}</h3>
        <span className="fw-500">{i18n.t("snippetDetail.deleted.message")}</span>
      </div>
    </div>
  );
};

export default SnippetDeletedMessage;
