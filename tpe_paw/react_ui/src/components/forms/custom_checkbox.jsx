const CustomCheckbox = (props) => {
  return (
    <label className="no-margin">
      <input
        className="mr-2"
        type="checkbox"
        value={props.value}
        onChange={props.onChange}
      />
      {props.label}
    </label>
  );
};

export default CustomCheckbox;
