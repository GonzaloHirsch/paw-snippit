import React from "react";
import Icon from "@mdi/react";

const TextInputFieldWithIcon = (props) => {
  const { type, iconPath, id, placeholder, htmlFor, onChange, errors } = props;
  return (
    <React.Fragment>
      <label htmlFor={htmlFor} className="sr-only">
        {placeholder}
      </label>
      <div className="input-group mb-2">
        <div className="input-group-prepend">
          <span className="input-group-text input-icon">
            <Icon path={iconPath} size={1} />
          </span>
        </div>
        <input
          type={type}
          id={id}
          className={"form-control m-0 " + (errors && "with-error")}
          placeholder={placeholder}
          autoFocus
          onChange={onChange}
          formNoValidate
          
        />{" "}
      </div>
      {errors && <span className="text-danger">{errors}</span>}
    </React.Fragment>
  );
};

export default TextInputFieldWithIcon;
