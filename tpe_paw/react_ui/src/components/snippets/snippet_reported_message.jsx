import React, { Component } from "react";
import i18n from "../../i18n";
import { mdiAlertOctagon, mdiCloseCircleOutline } from "@mdi/js";
import Icon from "@mdi/react";
import SnippetActionsClient from "../../api/implementations/SnippetActionsClient";
import { withRouter } from "react-router-dom";
import store from "../../store";

class ReportedSnippetMessage extends Component {
  snippetActionsClient;

  constructor(props) {
    super(props);
    this.snippetActionsClient = new SnippetActionsClient(store.getState().auth);
  }

  handleDismiss(id, dismissedReport) {
    // We trigger the dismiss, but don't care about the result
    this.snippetActionsClient
      .dismissReport(id)
      .then((res) => {
        // Call the dismiss report to let the parent know we dismissed
        dismissedReport();
      })
      .catch((e) => {
        // ERROR
      });
  }

  render() {
    const { id, dismissedReport } = this.props;
    return (
      <div className="row reported-message p-3 mb-3 align-items-vertical">
        <Icon
          className="text-muted reported-close-icon mt-2 mr-2"
          onClick={() => this.handleDismiss(id, dismissedReport)}
          path={mdiCloseCircleOutline}
          size={1}
        />
        <Icon className="mr-1 icon-reported" path={mdiAlertOctagon} size={3} />
        <div className="col no-padding mx-2">
          <h3 className="fw-700">{i18n.t("snippetDetail.reported.title")}</h3>
          <div className="fw-500">
            {i18n.t("snippetDetail.reported.message")}
          </div>
          <div className="fw-500 mt-1">
            {i18n.t("snippetDetail.reported.messageSuggestion")}
          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(ReportedSnippetMessage);
