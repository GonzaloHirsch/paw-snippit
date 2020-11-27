import React from "react";
import Icon from "@mdi/react";

const TextInputFieldWithIcon = (props) => {
  const { type, iconPath, id, placeholder, htmlFor, onChange } = props;
  return (
    <React.Fragment>
      <label htmlFor={htmlFor} className="sr-only">
        {placeholder}
      </label>
      <div className="input-group mb-3">
        <div className="input-group-prepend">
          <span className="input-group-text input-icon">
            <Icon path={iconPath} size={1} />
          </span>
        </div>
        <input
          type={type}
          id={id}
          className="form-control m-0"
          placeholder={placeholder}
          required
          autoFocus
          onChange={onChange}
        />{" "}
      </div>
    </React.Fragment>
  );
};

export default TextInputFieldWithIcon;
