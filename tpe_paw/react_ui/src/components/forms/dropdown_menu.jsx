import React from "react";

// Item with id "" is the hint and should be disabled and hidden
const DropdownMenu = (props) => {
  return (
    <select
      className="custom-select form-control rounded-border"
      id={props.id}
      onChange={props.onChange}
      value={props.value}
    >
      <option key={""} value={props.defaultValue}>
        {props.description}
      </option>
      <hr />
      {props.options.map((item, index) => (
        <option key={item.id} value={item.id}>
          {item.name}
        </option>
      ))}
    </select>
  );
};

export default DropdownMenu;
