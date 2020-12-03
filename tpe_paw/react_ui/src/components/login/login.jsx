import React, { Component } from "react";
import store from "../../store";
import { loginSuccess } from "../../redux/actions/actionCreators";
import { Link } from "react-router-dom";
import AuthClient from "../../api/implementations/AuthClient";
import i18n from "../../i18n";
import { mdiAccount, mdiLock } from "@mdi/js";
import TextInputFieldWithIcon from "./text_input_field_with_icon";
import CustomForm from "../forms/custom_form";
import {LOGIN_VALIDATIONS} from "../../js/validations"

class Login extends Component {
  state = {
    fields: {
      user: "",
      pass: "",
      remember: false,
    },
    errors: {
      user: null,
      pass: null,
    },
    responseErrors: null,
  };

  handleLogin() {
    const hasErrors = this.validateAll();

    if (!hasErrors) {
      // Get an instance of the cliente
      const authClient = new AuthClient();

      // Call the auth method
      authClient
        .login(this.state.fields.user, this.state.fields.pass)
        .then((res) => {
          // Get the token from the response
          // TODO: DETERMINE IF WE STORE THE REFRESH TOKEN
          const token = res.data.token;

          // Dispatch the login event
          store.dispatch(loginSuccess({ token }));

          console.log(this.props.history);
          // Push to home
          this.props.history.push("/");
        })
        .catch((e) => {
          if (e.response) {
            // client received an error response (5xx, 4xx)
            if (e.response.status === 401) {
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

  // Handles the change in one field, validates that field
  handleChange(e, name) {
    let fields = this.state.fields;
    fields[name] = e.target.value;
    let errors = this.state.errors;
    errors[name] = LOGIN_VALIDATIONS[name](fields[name]);
    this.setState({ fields: fields, errors: errors });
  }

  // Validates the whole form
  validateAll() {
    const fields = this.state.fields;
    let errors = {};
    for (let key in fields) {
      errors[key] = LOGIN_VALIDATIONS[key](fields[key]);
    }

    let hasErrors = false;
    for (let key in errors) {
      if (errors[key] !== null) {
        hasErrors = true;
      }
    }
    this.setState({ errors: errors });
    return hasErrors;
  }

  render() {
    return (
      <div className="form-signin rounded-lg">
        <CustomForm
          title={i18n.t("login.title")}
          action={i18n.t("login.form.action")}
          onSubmit={() => this.handleLogin()}
          generalError={this.state.responseErrors}
          includeAppName={true}
        >
          <TextInputFieldWithIcon
            id={"inputUsername"}
            htmlFor={"inputEmail"}
            type={"text"}
            placeholder={i18n.t("login.form.user")}
            iconPath={mdiAccount}
            onChange={(e) => this.handleChange(e, "user")}
            errors={this.state.errors.user}
          />

          <TextInputFieldWithIcon
            id={"inputPassword"}
            htmlFor={"inputPassword"}
            type={"password"}
            placeholder={i18n.t("login.form.pass")}
            iconPath={mdiLock}
            onChange={(e) => this.handleChange(e, "pass")}
            errors={this.state.errors.pass}
          />

          <div className="checkbox mb-3">
            <label>
              <input
                type="checkbox"
                value="remember-me"
                onChange={(e) => this.handleChange(e, "remember")}
              />{" "}
              {i18n.t("login.form.remember")}
            </label>
          </div>
        </CustomForm>

        <div
          className="my-2 text-white mx-auto"
          style={{ display: "block", textAlign: "center" }}
        >
          <span>
            {i18n.t("login.signup")}
            <Link to="/signup">
              <strong>{i18n.t("login.signupCall")}</strong>
            </Link>
          </span>
        </div>
        <div
          className="my-2 text-white mx-auto"
          style={{ display: "block", textAlign: "center" }}
        >
          <span>
            {i18n.t("login.recover")}
            <Link to="/recover">
              <strong>{i18n.t("login.recoverCall")}</strong>
            </Link>
          </span>
        </div>
      </div>
    );
  }
}

export default Login;
