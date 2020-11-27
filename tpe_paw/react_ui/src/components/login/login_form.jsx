import React from "react";
import i18n from "../../i18n";
import { mdiCodeTags } from "@mdi/js";
import Icon from "@mdi/react";

const LoginForm = (props) => {
  return (
    <form
      onSubmit={props.onSubmit} //Fix me!!
    >
      <span
        className="mx-auto text-white login-title"
        style={{ display: "block", textAlign: "center" }}
      >
        <Icon path={mdiCodeTags} size={2} />
        <span className="ml-2">
          {props.title}
          <strong>{i18n.t("app")}</strong>
        </span>
      </span>

      <div className="m-4 p-5 inner-square shadow rounded-lg">
        {props.children}

        <button
          className="btn btn-lg btn-primary btn-block mb-3 rounded-lg"
          type="submit"
        >
          {props.action}
        </button>
      </div>
    </form>
  );
};

export default LoginForm;
