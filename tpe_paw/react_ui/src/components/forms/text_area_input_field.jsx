import React from "react";

const TextAreaInputField = (props) => {
  const { type, id, placeholder, htmlFor, onChange, errors, cols, rows } = props;
  return (
    <React.Fragment>
      <label htmlFor={htmlFor} className="sr-only">
        {placeholder}
      </label>
      <textarea
        type={type}
        id={id}
        className={"form-control m-0 " + (errors && "with-error")}
        placeholder={placeholder}
        autoFocus
        onChange={onChange}
        formNoValidate
        cols={cols}
        rows={rows}
      />
      {errors && <span className="text-danger">{errors}</span>}
    </React.Fragment>
  );
};

export default TextAreaInputField;
