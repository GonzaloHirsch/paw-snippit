import React from "react";
import i18n from "../../i18n";
import { mdiCodeTags } from "@mdi/js";
import Icon from "@mdi/react";

const CustomForm = (props) => {
  return (
    <form noValidate onSubmit={props.onSubmit}>
      <span
        className="mx-auto text-white login-title"
        style={{ display: "flex", justifyContent: "center" }}
      >
        <Icon path={mdiCodeTags} size={2} />
        <span className="ml-2">
          {props.title}
          {props.includeAppName && <strong>{i18n.t("app")}</strong>}
        </span>
      </span>

      <div className="m-4 p-5 inner-square shadow rounded-border ">
        {props.children}

        {props.generalError && (
          <span className="text-danger">{props.generalError}</span>
        )}
        <button
          className="btn btn-lg btn-primary btn-block mb-3 rounded-border "
          type="submit"
        >
          {props.action}
        </button>
      </div>
    </form>
  );
};

export default CustomForm;
