import React from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

const CustomDatePicker = (props) => {
  //const [startDate, setStartDate] = useState(new Date());
  return (
    <DatePicker
      className={"form-control rounded-border " + (props.error && "with-error")}
      dateFormat="yyyy-MM-dd"
      selected={props.date}
      onChange={props.onChange}
      isClearable
      placeholderText={props.placeholder}
    />
  );
};

export default CustomDatePicker;
