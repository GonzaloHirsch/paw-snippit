import React from "react";

const InputField = (props) => {
  return (
    <input
      type={props.type == null ? "text" : props.type}
      className={"form-control rounded-border " + (props.error && "with-error")}
      placeholder={props.placeholder}
      aria-label={props.placeholder}
      aria-describedby="button-addon2"
      onChange={props.onChange}
      value={props.value}
    />
  );
};

export default InputField;
