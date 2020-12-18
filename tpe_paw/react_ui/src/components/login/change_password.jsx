import React, { Component } from "react";
import AuthClient from "../../api/implementations/AuthClient";
import i18n from "../../i18n";
import TextInputFieldWithIcon from "../forms/text_input_field_with_icon";
import CustomForm from "../forms/custom_form";
import { mdiCodeTags, mdiLock } from "@mdi/js";
import Icon from "@mdi/react";
import {
  CHANGE_PASS_VALIDATIONS,
  handleChange,
  validateAll,
  hasErrors,
} from "../../js/validations";
import { Helmet } from "react-helmet";
import { withRouter } from "react-router-dom";

class ChangePassword extends Component {
  authClient;

  state = {
    info: {
      id: 0,
      token: "",
    },
    fields: {
      newPassword: "",
      repeatNewPassword: "",
    },
    errors: {
      newPassword: null,
      repeatNewPassword: null,
    },
    responseErrors: null,
    changeOk: false,
    validToken: false,
  };

  constructor(props) {
    super(props);
    this.authClient = new AuthClient(this.props);
  }

  loadComponentBasedOnParams(params) {
    const id = params.get("id");
    const token = params.get("token");

    if (id !== this.state.info.id || token !== this.state.info.token) {
      // Storing the info in the url
      this.setState({ info: { id: id, token: token } });

      this.authClient
        .isTokenValid(id, token)
        .then((res) => {
          this.setState({ validToken: true });
        })
        .catch((e) => {
          let errorsToStore = null;
          if (e.response) {
            // client received an error response (5xx, 4xx)
            if (e.response.status === 400) {
              let errors = {};
              e.response.data.errors.forEach((error) => {
                for (let key in error) {
                  errors[key] = error[key];
                }
              });
              this.setState({ validToken: false, errors: errors });
            } else if (e.response.status === 401) {
              errorsToStore = i18n.t("login.form.errors.invalidGeneral");
            } else if (e.response.status === 500) {
              errorsToStore = i18n.t("errors.serverError");
            }
          } else if (e.request) {
            errorsToStore = i18n.t("errors.noConnection");
          } else {
            errorsToStore = i18n.t("errors.unknownError");
          }
          this.setState({ validToken: false, responseErrors: errorsToStore });
        });
    }
  }

  componentDidMount() {
    const params = new URLSearchParams(this.props.location.search);
    this.loadComponentBasedOnParams(params);
  }

  componentWillReceiveProps(nextProps) {
    const params = new URLSearchParams(nextProps.location.search);
    this.loadComponentBasedOnParams(params);
  }

  handleSubmit(event) {
    // Preventing the page reloading to avoid losing the variables
    event.preventDefault();
    const hasErrors = this._validateAll();

    if (!hasErrors) {
      this.authClient
        .changePassword(
          this.state.info.id,
          this.state.info.token,
          this.state.fields
        )
        .then((res) => {
          this.setState({ changeOk: true });
        })
        .catch((e) => {
          if (e.response) {
            // client received an error response (5xx, 4xx)
            if (e.response.status === 400) {
              let errors = {};
              e.response.data.errors.forEach((error) => {
                for (let key in error) {
                  errors[key] = error[key];
                }
              });
              this.setState({ changeOk: false, errors: errors });
            } else if (e.response.status === 401) {
              this.setState({
                changeOk: false,
                responseErrors: i18n.t("login.form.errors.invalidGeneral"),
              });
            } else if (e.response.status === 500) {
              this.setState({
                changeOk: false,
                responseErrors: i18n.t("errors.serverError"),
              });
            }
          } else if (e.request) {
            // client never received a response, or request never left
            this.setState({
              changeOk: false,
              responseErrors: i18n.t("errors.noConnection"),
            });
          } else {
            // anything else
            this.setState({
              changeOk: false,
              responseErrors: i18n.t("errors.unknownError"),
            });
          }
        });
    }
  }

  handleGoRecover() {
    this.props.history.push("/recover");
  }

  handleGoHome() {
    this.props.history.push("/");
  }

  handleGoLogin() {
    this.props.history.push("/login");
  }

  // Handles the change in one field, validates that field
  _handleChange(e, name) {
    const newState = handleChange(
      e,
      name,
      CHANGE_PASS_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(newState);
  }

  // Validates the whole form
  _validateAll() {
    const errors = validateAll(CHANGE_PASS_VALIDATIONS, this.state.fields);
    let hasErrorsToShow = hasErrors(errors);
    // Check if the passwords are equal
    if (
      !hasErrorsToShow &&
      this.state.fields.newPassword !== this.state.fields.repeatNewPassword
    ) {
      errors["repeatNewPassword"] = i18n.t(
        "changePassword.changeForm.errors.differentPasswords"
      );
      hasErrorsToShow = true;
    }
    this.setState({ errors: errors });
    return hasErrorsToShow;
  }

  getDisplayForm() {
    // Invalid token + id combination, not valid url
    if (!this.state.validToken) {
      return (
        <React.Fragment>
          <span
            className="mx-auto text-white login-title"
            style={{ display: "flex", justifyContent: "center" }}
          >
            <Icon path={mdiCodeTags} size={2} />
            <span className="ml-2">
              <strong>{i18n.t("app")}</strong>
            </span>
          </span>

          <div className="m-4 p-5 inner-square shadow rounded-lg">
            <span
              className="mb-2 align-items-horizontal-center fw-500"
              style={{ display: "flex", textAlign: "center", fontSize: "20px" }}
            >
              {i18n.t("changePassword.invalidLink.message")}
            </span>
            <button
              className="btn btn-lg form-button btn-block mb-3 rounded-lg"
              onClick={() => this.handleGoRecover()}
            >
              {i18n.t("changePassword.invalidLink.action")}
            </button>
          </div>
        </React.Fragment>
      );
    } else {
      // Didn't change pass yet or error in forms
      if (!this.state.changeOk) {
        return (
          <CustomForm
            title={i18n.t("changePassword.changeForm.title")}
            action={i18n.t("changePassword.changeForm.action")}
            onSubmit={(e) => this.handleSubmit(e)}
            generalError={this.state.responseErrors}
            includeAppName={false}
            loading={false}
          >
            <TextInputFieldWithIcon
              id={"inputPassword"}
              htmlFor={"inputPassword"}
              type={"password"}
              placeholder={i18n.t("changePassword.changeForm.password")}
              iconPath={mdiLock}
              onChange={(e) => this._handleChange(e, "newPassword")}
              errors={this.state.errors.newPassword}
            />

            <TextInputFieldWithIcon
              id={"inputRepeatPassword"}
              htmlFor={"inputRepeatPassword"}
              type={"password"}
              placeholder={i18n.t("changePassword.changeForm.repeatPassword")}
              iconPath={mdiLock}
              onChange={(e) => this._handleChange(e, "repeatNewPassword")}
              errors={this.state.errors.repeatNewPassword}
            />
          </CustomForm>
        );
      } else {
        return (
          <React.Fragment>
            <span
              className="mx-auto text-white login-title"
              style={{ display: "flex", justifyContent: "center" }}
            >
              <Icon path={mdiCodeTags} size={2} />
              <span className="ml-2">
                <strong>{i18n.t("app")}</strong>
              </span>
            </span>

            <div className="m-4 p-5 inner-square shadow rounded-lg">
              <span
                className="mb-2 align-items-horizontal-center fw-500"
                style={{
                  display: "flex",
                  textAlign: "center",
                  fontSize: "20px",
                }}
              >
                {i18n.t("changePassword.changeSuccess.message")}
              </span>
              <button
                className="btn btn-lg form-button btn-block mb-3 rounded-lg"
                onClick={() => this.handleGoHome()}
              >
                {i18n.t("changePassword.changeSuccess.actionGoHome")}
              </button>
              <button
                className="btn btn-lg form-button btn-block mb-3 rounded-lg"
                onClick={() => this.handleGoLogin()}
              >
                {i18n.t("changePassword.changeSuccess.actionGoLogin")}
              </button>
            </div>
          </React.Fragment>
        );
      }
    }
  }

  render() {
    return (
      <div className="form-signin rounded-lg">
        <Helmet>
          <title>
            {i18n.t("app")} | {i18n.t("nav.resetPassword")}
          </title>
        </Helmet>
        {this.getDisplayForm()}
      </div>
    );
  }
}

export default withRouter(ChangePassword);
