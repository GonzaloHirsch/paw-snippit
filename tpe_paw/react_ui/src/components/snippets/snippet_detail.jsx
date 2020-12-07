import React from "react";
import SnippetDangerMessage from "./snippet_danger_message";
import { Link } from "react-router-dom";
import i18n from "../../i18n";
import DetailBox from "./detail_box";
import LinkDetailBox from "./link_detail_box";
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import { Helmet } from "react-helmet";

const SnippetDetail = (props) => {
  const { snippet, language, creator } = props;
  console.log(snippet);
  return (
    <div className="flex-column detail-container mx-5 my-4 p-5 inner-square shadow rounded-lg">
      <Helmet>
        <title>
          {i18n.t("snippet", { count: 1 })} | {snippet.title}
        </title>
      </Helmet>
      {snippet.flagged && <SnippetDangerMessage />}
      <div className="row align-items-vertical no-margin mb-2">
        <h1 className="col no-padding">{snippet.title}</h1>
        <Link
          to={"/languages/" + language.id}
          className="language-snippet-tag-link p-2 flex-center rounded mr-1"
          style={{ fontSize: "20px" }}
        >
          {language.name.toUpperCase()}
        </Link>
      </div>
      <div className="row no-margin fw-500">
        <p>{snippet.description}</p>
      </div>
      <div className="dropdown-divider snippet-divider mb-4"></div>
      <div className="d-flex card-text card-snippet-block rounded px-3 py-2">
        <pre>
          <code>{snippet.code}</code>
        </pre>
        <p className="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
      </div>
      <div className="row align-items-horizontal-center flex-row mt-4 p-2">
        <DetailBox>
          <div className="row no-margin fw-500">
            <p>{creator.username}</p>
          </div>
          <div className="row no-margin fw-500">
            <p>{creator.id}</p>
          </div>
        </DetailBox>
        <DetailBox>
          <div className="row no-margin fw-500">
            <p>{creator.username}</p>
          </div>
          <div className="row no-margin fw-500">
            <p>{creator.id}</p>
          </div>
        </DetailBox>
        <LinkDetailBox path={"/user/" + creator.id}>
          <div className="row no-margin fw-500">
            <span>{snippet.createdDate}</span>
          </div>
          <div className="row no-margin align-items-vertical">
            <img src={getUserProfilePicUrl(creator)} alt="User Icon" />
            <div className="col flex-col align-items-vertical primary-text">
              <span className="fw-700">{creator.username}</span>
              <span className="fw-700">{creator.reputation}</span>
            </div>
          </div>
        </LinkDetailBox>
      </div>
    </div>
  );
};

export default SnippetDetail;
