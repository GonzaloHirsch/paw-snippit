import React from "react";

const SnippetDetail = (props) => {
  const { id, title, description, code, isFlagged, isDeleted } = props.snippet;
  return (
    <div className="flex-column detail-container mx-5 my-4 p-5 inner-square shadow rounded-lg">
      <h1>{"MY SNIPPET IS " + id}</h1>
      <h1>{"MY SNIPPET TITLE IS " + title}</h1>
      <p>{description}</p>
      <div className="d-flex card-text card-snippet-block rounded px-3 py-2">
        <pre>
          <code>{code}</code>
        </pre>
        <p className="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
      </div>
    </div>
  );
};

export default SnippetDetail;
