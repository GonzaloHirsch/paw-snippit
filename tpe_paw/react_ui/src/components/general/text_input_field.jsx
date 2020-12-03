import React from "react";

const TextInputField = (props) => {
  return (
    <input
      type="text"
      className="form-control"
      placeholder={props.placeholder}
      aria-label={props.placeholder}
      aria-describedby="button-addon2"
      onChange={props.onChange}
      value={props.value}
    />
  );
};

export default TextInputField;
