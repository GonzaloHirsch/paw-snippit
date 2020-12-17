import React, { Component } from "react";
import AuthClient from "../../api/implementations/AuthClient";
import i18n from "../../i18n";
import TextInputFieldWithIcon from "../forms/text_input_field_with_icon";
import { withRouter } from "react-router-dom";
import CustomForm from "../forms/custom_form";
import store from "../../store";
import * as actions from "../../redux/actions/actionTypes";
import { mdiKey } from "@mdi/js";
import {
  VERIFY_EMAIL_VALIDATIONS,
  handleChange,
  validateAll,
  hasErrors,
} from "../../js/validations";
import { Helmet } from "react-helmet";
import { Alert } from "reactstrap";
import ProfileVerifiedMessage from "./profile_verify_message";

class Verify extends Component {
  authClient;

  state = {
    info: {
      id: 0,
    },
    fields: {
      code: "",
    },
    errors: {
      code: null,
    },
    alertOpen: false,
    sendError: false,
    responseErrors: null,
  };

  constructor(props) {
    super(props);
    this.authClient = new AuthClient(props, store.getState().auth.token);
  }

  componentDidMount() {
    const params = new URLSearchParams(this.props.location.search);
    const id = params.get("id");

    // Storing the info in the url
    this.setState({ info: { id: id } });

    const storedData = store.getState().auth;

    if (storedData.status !== actions.LOGIN_SUCCESS) {
      // REDIRECT TO 404
    } else {
      if (storedData.info.uid !== id) {
        // REDIRECT to 403
      }
      // TODO: CHECK IF THE USER IS ALREADY VERIFIED OR NOT
    }
  }

  handleSubmit(event) {
    // Preventing the page reloading to avoid losing the variables
    event.preventDefault();
    const hasErrors = this._validateAll();

    if (!hasErrors) {
      this.authClient
        .verifyEmail(this.state.info.id, this.state.fields.code)
        .then((res) => {
          this.handleGoProfile();
        })
        .catch((e) => {
          if (e.response) {
            // client received an error response (5xx, 4xx)
            if (e.response.status === 400) {
              this.setState({
                errors: { code: i18n.t("verifyEmail.form.errors.invalidCode") },
              });
            } else if (e.response.status === 403) {
              // REDIRECT TO FORBIDDEN
            } else if (e.response.status === 404) {
              // REDIRECT TO NOT FOUND
            } else if (e.response.status === 500) {
              this.setState({
                changeOk: false,
                responseErrors: i18n.t("errors.serverError"),
              });
            }
          } else if (e.request) {
            this.setState({
              responseErrors: i18n.t("errors.noConnection"),
            });
          } else {
            this.setState({
              responseErrors: i18n.t("errors.unknownError"),
            });
          }
        });
    }
  }

  handleResendEmail() {
    this.authClient
      .sendVerifyEmail(this.state.info.id)
      .then((res) => {
        this.setState({ alertOpen: true, sendError: false });
      })
      .catch((e) => {
        this.setState({ alertOpen: true, sendError: true });
      });
  }

  handleGoProfile() {
    this.props.history.push("/profile");
  }

  // Handles the change in one field, validates that field
  _handleChange(e, name) {
    const newState = handleChange(
      e,
      name,
      VERIFY_EMAIL_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(newState);
  }

  // Validates the whole form
  _validateAll() {
    const errors = validateAll(VERIFY_EMAIL_VALIDATIONS, this.state.fields);
    this.setState({ errors: errors });
    return hasErrors(errors);
  }

  onDismiss() {
    this.setState({ alertOpen: false });
  }

  render() {
    return (
      <React.Fragment>
        <div className="form-signin rounded-lg">
          <Helmet>
            <title>{i18n.t("verifyEmail.docTitle")}</title>
          </Helmet>

          <Alert
            color={this.state.sendError ? "danger" : "success"}
            isOpen={this.state.alertOpen}
            toggle={() => this.onDismiss()}
          >
            {this.state.sendError
              ? i18n.t("verifyEmail.form.errors.invalidResend")
              : i18n.t("verifyEmail.form.validResend")}
          </Alert>

          <CustomForm
            title={i18n.t("verifyEmail.title")}
            action={i18n.t("verifyEmail.form.action")}
            onSubmit={(e) => this.handleSubmit(e)}
            generalError={this.state.responseErrors}
            includeAppName={false}
            loading={false}
          >
            <span
              className="mb-2 align-items-horizontal-center fw-500"
              style={{ display: "flex", textAlign: "center", fontSize: "20px" }}
            >
              {i18n.t("verifyEmail.message")}
            </span>
            <TextInputFieldWithIcon
              id={"inputCode"}
              htmlFor={"inputCode"}
              type={"text"}
              placeholder={i18n.t("verifyEmail.form.code")}
              iconPath={mdiKey}
              onChange={(e) => this._handleChange(e, "code")}
              errors={this.state.errors.code}
            />
          </CustomForm>
          <div
            className="my-2 text-white mx-auto"
            style={{ display: "block", textAlign: "center" }}
          >
            <span onClick={() => this.handleResendEmail()}>
              {i18n.t("verifyEmail.form.actionResend")}
            </span>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default withRouter(Verify);
