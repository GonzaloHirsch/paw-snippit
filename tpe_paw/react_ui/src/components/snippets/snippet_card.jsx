import React, { Component } from "react";
import { Link } from "react-router-dom";
import { mdiAlert } from "@mdi/js";
import Icon from "@mdi/react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";

class SnippetCard extends Component {
  state = {};

  render() {
    const { snippet } = this.props;
    const snippetLink = `/snippets/${snippet.id}`;
    return (
      <Link to={snippetLink} className="no-decoration">
        <div
          className="card-snippet-container card mb-3 bg-light bg-white rounded-border"
          style={{ maxWidth: "40rem" }}
        >
          <div className="card-header px-4" style={{ fontSize: "20px" }}>
            <div className="row align-items-center">
              <img
                src={getUserProfilePicUrl(snippet.creator)}
                alt="User Icon"
              />
              <div className="col ml-2">
                <div className="row primary-text" style={{ fontSize: "18px" }}>
                  {snippet.creator.username}
                </div>
                <div className="row text-muted" style={{ fontSize: "12px" }}>
                  {snippet.createdDate}
                </div>
              </div>
              {snippet.flagged && (
                <Icon
                  className="mr-1 icon-danger icon-size-2"
                  path={mdiAlert}
                  size={2}
                />
              )}
              <div
                className="language-snippet-tag p-2 flex-center rounded-border mr-1"
                style={{ fontSize: "20px" }}
              >
                {snippet.language.name.toUpperCase()}
              </div>
            </div>
          </div>
          <div className="card-body p-3">
            <h4 className="card-title">{snippet.title}</h4>
            <h6 className="card-subtitle mb-2 text-muted">
              {snippet.description}
            </h6>
            <div className="d-flex card-text card-snippet-block rounded-border px-3 py-2">
              <pre>
                <code>{snippet.code}</code>
              </pre>
              <p className="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
            </div>
          </div>
        </div>
      </Link>
    );
  }
}

export default SnippetCard;
