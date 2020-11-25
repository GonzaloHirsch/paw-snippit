import React, { Component } from "react";
import store from "../../store";
import { loginSuccess } from "../../redux/actions/actionCreators";
import { Link } from "react-router-dom";

import AuthClient from "../../api/implementations/AuthClient";
import TOKEN_HEADER from "../../api/Client";

// i18n
import i18n from "../../i18n";

// Icons
import Icon from "@mdi/react";
import { mdiCodeTags, mdiAccount, mdiLock } from "@mdi/js";

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
      <form
        className="form-signin rounded-lg"
        onSubmit={() => this.handleLogin()}
      >
        <span
          className="mx-auto text-white login-title"
          style={{ display: "block", textAlign: "center" }}
        >
          <Icon path={mdiCodeTags} size={2} />
          <span className="ml-2">
            {i18n.t("login.title")}
            <strong>{i18n.t("app")}</strong>
          </span>
        </span>
        <div className="m-4 p-5 inner-square shadow rounded-lg">
          <label htmlFor="inputEmail" className="sr-only">
            {i18n.t("login.form.user")}
          </label>
          <div className="input-group mb-3">
            <div className="input-group-prepend">
              <span className="input-group-text input-icon">
                <Icon path={mdiAccount} size={1} />
              </span>
            </div>
            <input
              type="text"
              id="inputUsername"
              className="form-control m-0"
              placeholder={i18n.t("login.form.user")}
              required
              autoFocus
              onChange={(e) => this.setState({ user: e.target.value })}
            />{" "}
          </div>
          <label htmlFor="inputPassword" className="sr-only">
            {i18n.t("login.form.pass")}
          </label>
          <div className="input-group mb-3">
            <div className="input-group-prepend">
              <span className="input-group-text input-icon">
                <Icon path={mdiLock} size={1} />
              </span>
            </div>
            <input
              type="password"
              id="inputPassword"
              className="form-control m-0"
              placeholder={i18n.t("login.form.pass")}
              required
              onChange={(e) => this.setState({ pass: e.target.value })}
            />
          </div>

          {/*  <div className="checkbox mb-3">
            <label className="flex-row checkbox-container">
              <input type="checkbox" value="remember-me" />{" "}
              <span className="checkbox-checkmark"></span>
              {i18n.t("login.form.remember")}
            </label>
          </div> */}

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
          <button
            className="btn btn-lg btn-primary btn-block mb-3 rounded-lg"
            type="submit"
          >
            {i18n.t("login.form.action")}
          </button>
        </div>
        <div
          className="my-2 text-white mx-auto"
          style={{ display: "block", textAlign: "center" }}
        >
          <span>
            {i18n.t("login.signup")}
            <Link to="/signup">
              <strong>{i18n.t("login.signup_call")}</strong>
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
              <strong>{i18n.t("login.recover_call")}</strong>
            </Link>
          </span>
        </div>
      </form>
    );
  }
}

export default Login;
