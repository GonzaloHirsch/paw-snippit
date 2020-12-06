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
      {props.options.map(function (item, index) {
        return item.id == "" ? (
          <option value={item.id} hidden disabled>
            {item.name}
          </option>
        ) : (
          <option value={item.id}>{item.name}</option>
        );
      })}
    </select>
  );
};

export default DropdownMenu;
