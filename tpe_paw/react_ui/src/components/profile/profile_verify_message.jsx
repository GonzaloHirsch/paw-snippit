import React, { Component } from 'react';
import i18n from "../../i18n";
import { mdiMessageAlert } from "@mdi/js";
import Icon from "@mdi/react";
import AuthClient from "../../api/implementations/AuthClient";
import { withRouter } from "react-router-dom";
import store from "../../store";

class ProfileVerifyMessage extends Component {
  authClient;

  constructor(props) {
    super(props);
    this.authClient = new AuthClient(store.getState().auth);
  }

  handleGoVerify() {
    // Getting the ID from the props
    const { id } = this.props;

    // We trigger email sending, but don't care for result, the user can resend email from the other page
    this.authClient
      .sendVerifyEmail(id)
      .then((res) => {
        // OK
      })
      .catch((e) => {
        // ERROR
      });

    // Adding the params to not lose the existing ones
    let params = new URLSearchParams();
    params.set("id", id);

    // Pushing the route
    this.props.history.push({
      pathname: "/verify",
      search: "?" + params.toString(),
    });
  }

  render() {
    const { id } = this.props;
    return (
      <div className="row verify-message p-3 mb-3 align-items-vertical">
        <Icon className="mr-1 icon-primary" path={mdiMessageAlert} size={3} />
        <div className="col">
          <h3 className="fw-700">{i18n.t("profile.verify.title")}</h3>
          <span className="fw-500">
            {i18n.t("profile.verify.message")}
            <span className="link-text" onClick={() => this.handleGoVerify()}>
              {i18n.t("profile.verify.action")}
            </span>
          </span>
        </div>
      </div>
    );
  }
}

export default withRouter(ProfileVerifyMessage);
