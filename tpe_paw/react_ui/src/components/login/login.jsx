import React, { Component } from "react";
import store from "../../store";
import { loginSuccess } from "../../redux/actions/actionCreators";
import { Link } from "react-router-dom";

import AuthClient from "../../api/implementations/AuthClient";

// i18n
import i18n from "../../i18n";

// Icons
import Icon from "@mdi/react";
import { mdiCodeTags, mdiAccount, mdiLock } from "@mdi/js";
import TextInputFieldWithIcon from "./text_input_field_with_icon";
import LoginForm from "./login_form";

class Login extends Component {
  state = {
    user: "",
    pass: "",
    remember: false,
  };

  handleLogin() {
    // Get an instance of the cliente
    const authClient = new AuthClient();

    // Call the auth method
    authClient
      .login(this.state.user, this.state.pass)
      .then((res) => {
        // Get the token from the response
        // TODO: DETERMINE IF WE STORE THE REFRESH TOKEN
        const token = res.data.token;

        // Dispatch the login event
        store.dispatch(loginSuccess({ token }));

        // Push to home
        this.props.history.push("/");
      })
      .catch((e) => {});
  }

  render() {
    return (
      <div className="form-signin rounded-lg">
        <LoginForm
          title={i18n.t("login.title")}
          action={i18n.t("login.form.action")}
          onSubmit={() => this.handleLogin()}
        >
          <TextInputFieldWithIcon
            id={"inputUsername"}
            htmlFor={"inputEmail"}
            type={"text"}
            placeholder={i18n.t("login.form.user")}
            iconPath={mdiAccount}
            onChange={(e) => this.setState({ user: e.target.value })}
          />

          <TextInputFieldWithIcon
            id={"inputPassword"}
            htmlFor={"inputPassword"}
            type={"password"}
            placeholder={i18n.t("login.form.pass")}
            iconPath={mdiLock}
            onChange={(e) => this.setState({ pass: e.target.value })}
          />

          <div className="checkbox mb-3">
            <label>
              <input
                type="checkbox"
                value="remember-me"
                onChange={(e) => this.setState({ remember: e.target.value })}
              />{" "}
              {i18n.t("login.form.remember")}
            </label>
          </div>
        </LoginForm>

        {/*  <div className="checkbox mb-3">
            <label className="flex-row checkbox-container">
              <input type="checkbox" value="remember-me" />{" "}
              <span className="checkbox-checkmark"></span>
              {i18n.t("login.form.remember")}
            </label>
          </div> */}

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
