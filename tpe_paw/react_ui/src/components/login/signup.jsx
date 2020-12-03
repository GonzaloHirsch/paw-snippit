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
import { mdiEmail, mdiAccount, mdiLock } from "@mdi/js";
import TextInputFieldWithIcon from "./text_input_field_with_icon";
import CustomForm from "../forms/custom_form";

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
      <div className="form-signin rounded-lg">
        <CustomForm
          title={i18n.t("signup.title")}
          action={i18n.t("signup.form.action")}
          onSubmit={() => this.handleLogin()}
          includeAppName={true}
        >
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
            iconPath={mdiEmail}
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
