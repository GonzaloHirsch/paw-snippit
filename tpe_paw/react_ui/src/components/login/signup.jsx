import React, { Component } from "react";
import store from "../../store";
import { loginSuccess } from "../../redux/actions/actionCreators";
import { Link } from "react-router-dom";
import AuthClient from "../../api/implementations/AuthClient";
import i18n from "../../i18n";
import { mdiEmail, mdiAccount, mdiLock } from "@mdi/js";
import TextInputFieldWithIcon from "../forms/text_input_field_with_icon";
import CustomForm from "../forms/custom_form";
import {
  REGISTER_VALIDATIONS,
  handleChange,
  validateAll,
  hasErrors,
} from "../../js/validations";

class SignUp extends Component {
  state = {
    fields: {
      username: "",
      email: "",
      password: "",
      repeatPassword: "",
    },
    errors: {
      username: null,
      email: null,
      password: null,
      repeatPassword: null,
    },
    responseErrors: null,
    loading: false,
  };

  handleSubmit() {
    const hasErrors = this._validateAll();

    if (!hasErrors) {
      // Get an instance of the cliente
      const authClient = new AuthClient();

      this.setState({ loading: true });

      // Call the auth method
      authClient
        .signup(
          this.state.fields.username,
          this.state.fields.email,
          this.state.fields.password,
          this.state.fields.repeatPassword
        )
        .then((res) => {
          // We should not have problems with this request, the user is fresh and credentials ok
          authClient
            .login(this.state.fields.username, this.state.fields.password)
            .then((res) => {
              // Get the token from the response
              // TODO: DETERMINE IF WE STORE THE REFRESH TOKEN
              const token = res.data.token;

              // Dispatch the login event
              // We add the remember to be true
              store.dispatch(loginSuccess({ token }, true));

              // Push to home
              this.props.history.push("/");
            });
        })
        .catch((e) => {
          let responseErrors = null;
          if (e.response) {
            // client received an error response (5xx, 4xx)
            if (e.response.status === 400) {
              let errors = {};
              e.response.data.errors.forEach((error) => {
                for (let key in error) {
                  errors[key] = error[key];
                }
              });
              this.setState({ errors: errors, loading: false });
            } else if (e.response.status === 401) {
              responseErrors = i18n.t("login.form.errors.invalidGeneral");
            } else if (e.response.status === 500) {
              responseErrors = i18n.t("errors.serverError");
            }
          } else if (e.request) {
            responseErrors = i18n.t("errors.noConnection");
          } else {
            responseErrors = i18n.t("errors.unknownError");
          }
          this.setState({
            responseErrors: i18n.t("errors.serverError"),
            loading: false,
          });
        });
    }
  }

  // Handles the change in one field, validates that field
  _handleChange(e, name) {
    const newState = handleChange(
      e,
      name,
      REGISTER_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(newState);
  }

  // Validates the whole form
  _validateAll() {
    const errors = validateAll(REGISTER_VALIDATIONS, this.state.fields);
    let hasErrorsToShow = hasErrors(errors);
    // Check if the passwords are equal
    if (
      !hasErrorsToShow &&
      this.state.fields.password !== this.state.fields.repeatPassword
    ) {
      errors["repeatPassword"] = i18n.t(
        "changePassword.changeForm.errors.differentPasswords"
      );
      hasErrorsToShow = true;
    }
    this.setState({ errors: errors });
    return hasErrors(errors);
  }

  render() {
    return (
      <div className="form-signin rounded-border">
        <CustomForm
          title={i18n.t("signup.title")}
          action={i18n.t("signup.form.action")}
          onSubmit={(e) => this.handleSubmit(e)}
          includeAppName={true}
          loading={this.state.loading}
        >
          <TextInputFieldWithIcon
            id={"inputUsername"}
            htmlFor={"inputUsername"}
            type={"text"}
            placeholder={i18n.t("signup.form.user")}
            iconPath={mdiAccount}
            onChange={(e) => this._handleChange(e, "username")}
            errors={this.state.errors.username}
          />

          <TextInputFieldWithIcon
            id={"inputEmail"}
            htmlFor={"inputEmail"}
            type={"text"}
            placeholder={i18n.t("signup.form.email")}
            iconPath={mdiEmail}
            onChange={(e) => this._handleChange(e, "email")}
            errors={this.state.errors.email}
          />

          <TextInputFieldWithIcon
            id={"inputPassword"}
            htmlFor={"inputPassword"}
            type={"password"}
            placeholder={i18n.t("signup.form.pass")}
            iconPath={mdiLock}
            onChange={(e) => this._handleChange(e, "password")}
            errors={this.state.errors.password}
          />

          <TextInputFieldWithIcon
            id={"inputRepeatPassword"}
            htmlFor={"inputRepeatPassword"}
            type={"password"}
            placeholder={i18n.t("signup.form.repeatPass")}
            iconPath={mdiLock}
            onChange={(e) => this._handleChange(e, "repeatPassword")}
            errors={this.state.errors.repeatPassword}
          />
        </CustomForm>

        <div
          className="my-2 text-white mx-auto"
          style={{ display: "block", textAlign: "center" }}
        >
          <span>
            {i18n.t("signup.login")}
            <Link to="/login">
              <strong>{i18n.t("signup.loginCall")}</strong>
            </Link>
          </span>
        </div>
      </div>
    );
  }
}

export default SignUp;
