import React from "react";
import { Link } from "react-router-dom";
import { mdiAlert, mdiHeart, mdiHeartOutline } from "@mdi/js";
import Icon from "@mdi/react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import SyntaxHighlighter from "react-syntax-highlighter";
import { googlecode } from "react-syntax-highlighter/dist/esm/styles/hljs";
import { getDateFromAPIString } from "../../js/date_utils";

const SnippetCard = (props) => {
  const { snippet, handleFav, userIsLogged } = props;
  const snippetLink = `/snippets/${snippet.id}`;
  return (
    <Link to={snippetLink} className="no-decoration">
      <div className="card-snippet-container card mb-3 bg-light bg-white rounded-border">
        <div className="card-header px-3" style={{ fontSize: "20px" }}>
          <div className="row align-items-center no-margin">
            <img src={getUserProfilePicUrl(snippet.creator)} alt="User Icon" />
            <div className="col ml-2">
              <div className="row primary-text" style={{ fontSize: "18px" }}>
                {snippet.creator.username}
              </div>
              <div className="row text-muted" style={{ fontSize: "12px" }}>
                {getDateFromAPIString(snippet.createdDate)}
              </div>
            </div>
            {userIsLogged && (
              <Icon
                className={
                  "mr-1 icon-size-2 icon-fav" +
                  (snippet.favorite ? "-selected" : "")
                }
                path={snippet.favorite ? mdiHeart : mdiHeartOutline}
                size={2}
                onClick={(e) => handleFav(e, snippet.id)}
              />
            )}
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
          <div className="d-flex card-text card-snippet-block rounded px-3 py-2">
            <SyntaxHighlighter
              wrapLongLines={true}
              showInlineLineNumbers={false}
              showLineNumbers={false}
              language={snippet.language.name}
              style={googlecode}
            >
              {snippet.code}
            </SyntaxHighlighter>
            <p className="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
          </div>
        </div>
      </div>
    </Link>
  );
};

export default SnippetCard;
