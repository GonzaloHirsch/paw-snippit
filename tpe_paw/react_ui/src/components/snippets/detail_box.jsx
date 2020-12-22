import React from "react";

const DetailBox = (props) => {
  return <div className="boxed-content shadow p-3 m-2 flex-col align-items-vertical">{props.children}</div>;
};

export default DetailBox;
