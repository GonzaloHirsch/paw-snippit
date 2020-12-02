import React, { Component } from "react";
import AuthClient from "../../api/implementations/AuthClient";
import i18n from "../../i18n";
import { mdiEmail } from "@mdi/js";
import TextInputFieldWithIcon from "./text_input_field_with_icon";
import CustomForm from "../forms/custom_form";
import {
  RECOVER_SEND_VALIDATIONS,
  handleChange,
  validateAll,
  hasErrors,
} from "../../js/validations";

class RecoverSend extends Component {
  state = {
    fields: {
      email: "",
    },
    errors: {
      email: null,
    },
    responseErrors: null,
    sentEmail: false,
  };

  handleSubmit() {
    const hasErrors = this._validateAll();

    if (!hasErrors) {
      // Get an instance of the cliente
      const authClient = new AuthClient();

      // Call the auth method
      authClient
        .sendRecoveryEmail(this.state.fields.email)
        .then((res) => {
          this.setState({ sentEmail: true });
        })
        .catch((e) => {
          if (e.response) {
            // client received an error response (5xx, 4xx)
            if (e.response.status === 400) {
              let errors = {};
              console.log(e);
              console.log(e.response.data.errors);
              e.response.data.errors.forEach((error) => {
                for (let key in error) {
                  errors[key] = error[key];
                }
              });
              this.setState({ errors: errors });
            } else if (e.response.status === 401) {
              this.setState({
                responseErrors: i18n.t("login.form.errors.invalidGeneral"),
              });
            } else if (e.response.status === 500) {
              this.setState({ responseErrors: i18n.t("errors.serverError") });
            }
          } else if (e.request) {
            // client never received a response, or request never left
            this.setState({ responseErrors: i18n.t("errors.noConnection") });
          } else {
            // anything else
            this.setState({ responseErrors: i18n.t("errors.unknownError") });
          }
        });
    }
  }

  handleGoHome() {
    this.props.history.push("/");
  }

  // Handles the change in one field, validates that field
  _handleChange(e, name) {
    const newState = handleChange(
      e,
      name,
      RECOVER_SEND_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(newState);
  }

  // Validates the whole form
  _validateAll() {
    const errors = validateAll(RECOVER_SEND_VALIDATIONS, this.state.fields);
    this.setState({ errors: errors });
    return hasErrors(errors);
  }

  getDisplayForm() {
    if (this.state.sentEmail) {
      return (
        <CustomForm
          title=""
          action={i18n.t("recover.form.afterAction")}
          onSubmit={() => this.handleGoHome()}
          generalError={this.state.responseErrors}
          includeAppName={true}
        >
          <span
            className="mb-2 align-items-horizontal-center fw-500"
            style={{ display: "flex", textAlign: "center", fontSize: "20px" }}
          >
            {i18n.t("recover.afterMessage")}
          </span>
        </CustomForm>
      );
    } else {
      return (
        <CustomForm
          title={i18n.t("recover.title")}
          action={i18n.t("recover.form.action")}
          onSubmit={() => this.handleSubmit()}
          generalError={this.state.responseErrors}
          includeAppName={false}
        >
          <span
            className="mb-2 align-items-horizontal-center fw-500"
            style={{ display: "flex", textAlign: "center", fontSize: "20px" }}
          >
            {i18n.t("recover.message")}
          </span>
          <TextInputFieldWithIcon
            id={"inputEmail"}
            htmlFor={"inputEmail"}
            type={"text"}
            placeholder={i18n.t("recover.form.email")}
            iconPath={mdiEmail}
            onChange={(e) => this._handleChange(e, "email")}
            errors={this.state.errors.email}
          />
        </CustomForm>
      );
    }
  }

  render() {
    return (
      <div className="form-signin rounded-lg">{this.getDisplayForm()}</div>
    );
  }
}

export default RecoverSend;
