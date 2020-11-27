/// To do -> hice copy paste de login imports
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
import TextInputFieldWithIcon from "./text_input_field_with_icon";

class SignUp extends Component {
  state = {
    user: "",
    email: "",
    pass: "",
    repeatedPass: "",
  };

  // TODO -> Methods

  render() {
    return (
      <form
        className="form-signin rounded-lg"
        onSubmit={() => this.handleLogin()} //Fix me!!
      >
        <span
          className="mx-auto text-white login-title"
          style={{ display: "block", textAlign: "center" }}
        >
          <Icon path={mdiCodeTags} size={2} />
          <span className="ml-2">
            {i18n.t("signup.title")}
            <strong>{i18n.t("app")}</strong>
          </span>
        </span>
        <div className="m-4 p-5 inner-square shadow rounded-lg">
          <TextInputFieldWithIcon
            id={"inputUsername"}
            htmlFor={"inputEmail"}
            type={"text"}
            placeholder={i18n.t("signup.form.user")}
            iconPath={mdiAccount}
            onChange={(e) => this.setState({ user: e.target.value })}
          />

          <TextInputFieldWithIcon
            id={"inputEmail"}
            htmlFor={"inputEmail"}
            type={"text"}
            placeholder={i18n.t("signup.form.email")}
            iconPath={mdiAccount}
            onChange={(e) => this.setState({ email: e.target.value })}
          />

          <TextInputFieldWithIcon
            id={"inputPassword"}
            htmlFor={"inputPassword"}
            type={"password"}
            placeholder={i18n.t("signup.form.pass")}
            iconPath={mdiLock}
            onChange={(e) => this.setState({ pass: e.target.value })}
          />

          <TextInputFieldWithIcon
            id={"inputPassword"}
            htmlFor={"inputPassword"}
            type={"password"}
            placeholder={i18n.t("signup.form.repeatPass")}
            iconPath={mdiLock}
            onChange={(e) => this.setState({ repeatedPass: e.target.value })}
          />

          <button
            className="btn btn-lg btn-primary btn-block mb-3 rounded-lg"
            type="submit"
          >
            {i18n.t("signup.form.action")}
          </button>
        </div>
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
      </form>
    );
  }
}

export default SignUp;
