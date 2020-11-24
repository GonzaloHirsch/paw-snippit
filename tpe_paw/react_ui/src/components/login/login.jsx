import React, { Component } from "react";
import store from "../../store";
import { loginSuccess } from "../../redux/actions/actionCreators";
import { Link } from "react-router-dom";

// i18n
import i18n from "../../i18n";

// Icons
import Icon from "@mdi/react";
import { mdiCodeTags, mdiAccount, mdiLock } from "@mdi/js";

class Login extends Component {
  state = {};
  render() {
    // To get the state
    store.getState();

    // To dispatch, the param is the token inside the function
    store.dispatch(loginSuccess({}));
    return (
      <form className="form-signin rounded-lg">
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
          <label for="inputEmail" className="sr-only">
            {i18n.t("login.form.user")}
          </label>
          <div className="input-group mb-3">
            <div className="input-group-prepend">
              <span className="input-group-text input-icon">
                <Icon path={mdiAccount} size={1} />
              </span>
            </div>
            <input
              type="email"
              id="inputEmail"
              className="form-control m-0"
              placeholder={i18n.t("login.form.user")}
              required
              autofocus
            />{" "}
          </div>
          <label for="inputPassword" className="sr-only">
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
            />
          </div>
          <div className="checkbox mb-3">
            <label>
              <input type="checkbox" value="remember-me" />{" "}
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
