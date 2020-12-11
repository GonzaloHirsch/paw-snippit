import React, { Component } from "react";

// Components
import SnippetDangerMessage from "./snippet_danger_message";
import SnippetDeletedMessage from "./snippet_deleted_message";
import SnippetReportedMessage from "./snippet_reported_message";
import { Link } from "react-router-dom";
import DetailBox from "./detail_box";
import LinkDetailBox from "./link_detail_box";
import Icon from "@mdi/react";
import { Helmet } from "react-helmet";
import SyntaxHighlighter from "react-syntax-highlighter";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  UncontrolledTooltip,
} from "reactstrap";
import TextAreaInputField from "../forms/text_area_input_field";
import ItemFeed from "../items/item_feed";

// JS Utils
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import {
  REPORT_VALIDATIONS,
  handleChange,
  validateAll,
  hasErrors,
} from "../../js/validations";
import { ITEM_TYPES } from "../../js/constants";

// Others
import i18n from "../../i18n";
import {
  mdiAlertOctagon,
  mdiContentCopy,
  mdiDelete,
  mdiFlag,
  mdiFlagOutline,
  mdiHeart,
  mdiHeartOutline,
  mdiDeleteRestore,
  mdiThumbUp,
  mdiThumbUpOutline,
  mdiThumbDown,
  mdiThumbDownOutline
} from "@mdi/js";
import { googlecode } from "react-syntax-highlighter/dist/esm/styles/hljs";

class SnippetDetail extends Component {
  state = {
    isReporting: false,
    fields: {
      message: "",
    },
    errors: {
      message: null,
    },
  };

  // Handles the change in one field, validates that field
  _handleChange(e, name) {
    const newState = handleChange(
      e,
      name,
      REPORT_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(newState);
  }

  // Validates the whole form
  _validateAll() {
    const errors = validateAll(REPORT_VALIDATIONS, this.state.fields);
    this.setState({ errors: errors });
    return hasErrors(errors);
  }

  setReporting = () => {
    this.setState({ isReporting: !this.state.isReporting });
  };

  handleReportSubmit(id) {
    const hasErrors = this._validateAll();

    if (!hasErrors) {
      this.props.handleReport(id, this.state.fields.message);
      this.setReporting();
    }
  }

  copyToClipboard() {
    let textBox = document.getElementById("hidden-code-input");
    textBox.select();
    document.execCommand("copy");
  }

  getReportedSnippetBox(snippet) {
    if (this.props.userCanReport && snippet.userReported) {
      return (
        <DetailBox>
          <Icon
            className="row no-margin icon-reported"
            path={mdiAlertOctagon}
            size={3}
          />
        </DetailBox>
      );
    } else {
      return (
        <DetailBox>
          <Icon
            className={
              "row no-margin icon-reported-clickable" +
              (snippet.userReported ? "-selected" : "")
            }
            path={mdiAlertOctagon}
            size={3}
            onClick={this.setReporting}
          />
        </DetailBox>
      );
    }
  }

  render() {
    const {
      snippet,
      language,
      creator,
      tags,
      dismissedReport,
      handleFav,
      userCanReport,
      userIsOwner,
      userIsAdmin,
      handleFlag,
      handleDelete,
      handleLike,
      handleDislike
    } = this.props;
    return (
      <div className="flex-column detail-container mx-5 my-4 p-5 inner-square shadow rounded-lg">
        <Helmet>
          <title>
            {i18n.t("snippet", { count: 1 })} | {snippet.title}
          </title>
        </Helmet>

        <Modal isOpen={this.state.isReporting} toggle={this.setReporting}>
          <ModalHeader toggle={this.setReporting}>
            {i18n.t("snippetDetail.reporting.title")}
          </ModalHeader>
          <ModalBody>
            <div className="my-2">
              {i18n.t("snippetDetail.reporting.message")}
            </div>
            <TextAreaInputField
              id={"inputMessage"}
              htmlFor={"inputMessage"}
              type={"text"}
              placeholder={i18n.t("snippetDetail.reporting.form.hint")}
              onChange={(e) => this._handleChange(e, "message")}
              errors={this.state.errors.message}
              rows={7}
              cols={50}
            />
          </ModalBody>
          <ModalFooter>
          <Button color="danger" onClick={this.setReporting}>
              {i18n.t("snippetDetail.reporting.actionCancel")}
            </Button>
            <Button
              className="form-button"
              onClick={() => this.handleReportSubmit(snippet.id)}
            >
              {i18n.t("snippetDetail.reporting.actionConfirm")}
            </Button>
          </ModalFooter>
        </Modal>

        {snippet.deleted && <SnippetDeletedMessage />}
        {snippet.flagged && <SnippetDangerMessage />}
        {snippet.reported && !snippet.deleted && userIsOwner && (
          <SnippetReportedMessage
            id={snippet.id}
            dismissedReport={dismissedReport}
          />
        )}
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
        <div className="d-flex card-text snippet-code-block rounded px-3 py-2 mb-2">
          <Icon
            id="copyCodeButton"
            className="snippet-code-copy-button mt-1 mr-1 p-1 icon-size-2"
            path={mdiContentCopy}
            size={2}
            onClick={(e) => this.copyToClipboard(e)}
          />
          <UncontrolledTooltip
            placement="top"
            target="copyCodeButton"
            trigger="click"
            autohide={true}
          >
            {i18n.t("snippetDetail.copied")}
          </UncontrolledTooltip>

          <SyntaxHighlighter
            wrapLongLines={true}
            showInlineLineNumbers={false}
            showLineNumbers={false}
            language={language.name}
            style={googlecode}
          >
            {snippet.code}
          </SyntaxHighlighter>
          <textarea
            id="hidden-code-input"
            className="hidden-code"
            value={snippet.code}
            readOnly
          />
          <p className="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
        </div>
        {tags.length > 0 && (
          <ItemFeed
            items={tags}
            currentPage={0}
            hasPagination={false}
            type={ITEM_TYPES.TAG}
          />
        )}

        <div className="row align-items-horizontal-center flex-row mt-4 p-2">
          {userIsAdmin && (
            <DetailBox>
              <Icon
                className={
                  "row no-margin icon-fav" +
                  (snippet.flagged ? "-selected" : "")
                }
                path={snippet.flagged ? mdiFlag : mdiFlagOutline}
                size={3}
                onClick={(e) => handleFlag(e, snippet.id)}
              />
            </DetailBox>
          )}
          {userIsOwner && (
            <DetailBox>
              <Icon
                className="row no-margin icon-delete"
                path={snippet.deleted ? mdiDeleteRestore : mdiDelete}
                size={3}
                onClick={(e) => handleDelete(e, snippet.id)}
              />
            </DetailBox>
          )}
          <DetailBox>
            <Icon
              className={
                "row no-margin icon-fav" + (snippet.favorite ? "-selected" : "")
              }
              path={snippet.favorite ? mdiHeart : mdiHeartOutline}
              size={3}
              onClick={(e) => handleFav(e, snippet.id)}
            />
          </DetailBox>
          {userCanReport && !userIsOwner && this.getReportedSnippetBox(snippet)}
          <DetailBox>
            <div className="row no-margin fw-500">
            <Icon
              className={
                "row no-margin icon-like" + (snippet.userVotedPositive ? "-selected" : "")
              }
              path={snippet.userVotedPositive ? mdiThumbUp : mdiThumbUpOutline}
              size={3}
              onClick={(e) => handleLike(e, snippet.id)}
            />
            <span className="mx-2 align-items-vertical vote-count">{snippet.voteCount}</span>
            <Icon
              className={
                "row no-margin icon-dislike" + (snippet.userVotedNegative ? "-selected" : "")
              }
              path={snippet.userVotedNegative ? mdiThumbDown : mdiThumbDownOutline}
              size={3}
              onClick={(e) => handleDislike(e, snippet.id)}
            />
            </div>
          </DetailBox>
          <LinkDetailBox path={"/user/" + creator.id}>
            <div className="row mb-2 fw-500 primary-text no-decoration">
              <span>{i18n.t("snippetDetail.uploaded", {date: snippet.createdDate})}</span>
            </div>
            <div className="row no-margin align-items-vertical">
              <img src={getUserProfilePicUrl(creator)} alt="User Icon" />
              <div className="col flex-col align-items-vertical primary-text">
                <span className="fw-700">{creator.username}</span>
                <span className="fw-700">{creator.stats.reputation}</span>
              </div>
            </div>
          </LinkDetailBox>
        </div>
      </div>
    );
  }
}

export default SnippetDetail;
