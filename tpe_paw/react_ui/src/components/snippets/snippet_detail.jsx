import React from "react";

const SnippetDetail = (props) => {
  const { id, title, description, code, isFlagged, isDeleted } = props.snippet;
  return (
    <React.Fragment>
      <h1>{"MY SNIPPET IS " + id}</h1>;<h1>{"MY SNIPPET TITLE IS " + title}</h1>
    </React.Fragment>
  );
};

export default SnippetDetail;
