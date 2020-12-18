import React, { Component } from "react";
import { Typeahead } from "react-bootstrap-typeahead"; // ES2015
import "react-bootstrap-typeahead/css/Typeahead.css";
import SnippetClient from "../../api/implementations/SnippetClient";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import TextAreaInputField from "../forms/text_area_input_field";
import i18n from "../../i18n";
import { withRouter } from "react-router";
import { Alert } from "reactstrap";
import {
  handleChange,
  validateAll,
  hasErrors,
  SNIPPET_CREATE_VALIDATIONS,
} from "../../js/validations";
import { extractResourceIdFromHeaders } from "../../js/api_utils";

class SnippetCreateForm extends Component {
  snippetClient;
  languagesAndTagsClient;

  constructor(props) {
    super(props);
    this.snippetClient = new SnippetClient(this.props, this.props.token);
    this.languagesAndTagsClient = new LanguagesAndTagsClient(this.props);
    this.state = {
      fields: {
        title: "",
        description: "",
        code: "",
        language: [],
        tags: [],
      },
      errors: {
        title: null,
        description: null,
        code: null,
        language: null,
        tags: null,
      },
      options: {
        languages: [],
        tags: [],
      },
      loading: false,
      alert: {
        show: false,
        message: "",
      },
    };
  }

  _loadLanguages() {
    this.languagesAndTagsClient
      .getLanguageList()
      .then((res) => {
        let options = { ...this.state.options };
        options.languages = res.data;
        this.setState({
          options: options,
        });
      })
      .catch((e) => {});
  }

  _loadTags() {
    this.languagesAndTagsClient
      .getTagList()
      .then((res) => {
        let options = { ...this.state.options };
        options.tags = res.data;
        this.setState({
          options: options,
        });
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this._loadLanguages();
    this._loadTags();
  }

  _onTypeaheadChange(selected, name) {
    let fields = { ...this.state.fields };
    let errors = { ...this.state.errors };
    fields[name] = selected;
    errors[name] = SNIPPET_CREATE_VALIDATIONS[name](fields[name]);
    this.setState({ fields: fields, errors: errors });
  }

  _onInputChange(e, name) {
    const state = handleChange(
      e,
      name,
      SNIPPET_CREATE_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(state);
  }

  _onSubmit() {
    const errors = validateAll(SNIPPET_CREATE_VALIDATIONS, this.state.fields);

    if (!hasErrors(errors)) {
      this.setState({ loading: true });
      this._createSnippet();
    } else {
      this.setState({ errors: errors });
    }
  }

  _createSnippet() {
    const snippet = { ...this.state.fields };
    snippet.language = snippet.language[0].id;
    snippet.tags = snippet.tags.map((tag) => tag.id);
    this.snippetClient
      .postCreateSnippet(snippet)
      .then((res) => {
        this.props.history.push(
          "/snippets/" + extractResourceIdFromHeaders(res.headers)
        ); // On success, redirect to the snippet
      })
      .catch((e) => {
        if (e.response) {
          // Error is not 401, 403, 404 or 500
          const alert = {
            show: true,
            message: i18n.t("errors.snippetCreate"),
          };
          this.setState({ alert: alert });
        }
        this.setState({ loading: false });
      });
  }

  // To dismiss the alert in case of error
  onDismiss = () => {
    const alert = { show: false, message: "" };
    this.setState({ alert: alert });
  };

  render() {
    const { languages, tags } = this.state.options;
    const { fields, errors } = this.state;
    const textareaColumns = 60;
    return (
      <form
        onSubmit={() => this._onSubmit()}
        className="d-flex flex-column justify-space-between"
      >
        <div className="parent-width d-flex flex-row mb-3">
          <div
            className="d-flex flex-column"
            style={{ width: "65%", marginRight: "30px" }}
          >
            <h5>{i18n.t("snippetCreate.fields.title")}</h5>
            <input
              id="snippetTitleInput"
              type="text"
              className={"form-control  " + (errors.title && "with-error")}
              placeholder={i18n.t("snippetCreate.placeholders.title")}
              onChange={(e) => this._onInputChange(e, "title")}
              value={fields.title}
            />
            {errors.title && (
              <span className="text-danger">{errors.title}</span>
            )}
          </div>
          <div className="d-flex flex-column" style={{ width: "30%" }}>
            <h5>{i18n.t("snippetCreate.fields.language")}</h5>
            <div className={errors.language && "error-container"}>
              <Typeahead
                id="chooseLanguage"
                placeholder={i18n.t("snippetCreate.placeholders.language")}
                options={languages}
                labelKey="name"
                onChange={(selected) =>
                  this._onTypeaheadChange(selected, "language")
                }
                selected={fields.language}
              />
            </div>
            {errors.language && (
              <span className="text-danger">{errors.language}</span>
            )}
          </div>
        </div>
        <div className="mb-3">
          <h5>{i18n.t("snippetCreate.fields.description")}</h5>
          <TextAreaInputField
            id={"inputDescription"}
            htmlFor={"inputDescription"}
            type={"text"}
            placeholder={i18n.t("snippetCreate.placeholders.description")}
            onChange={(e) => this._onInputChange(e, "description")}
            errors={this.state.errors.description}
            rows={4}
            cols={textareaColumns}
          />
        </div>
        <div className="mb-3">
          <h5>{i18n.t("snippetCreate.fields.code")}</h5>
          <TextAreaInputField
            id={"inputDescription"}
            htmlFor={"inputDescription"}
            type={"text"}
            placeholder={i18n.t("snippetCreate.placeholders.code")}
            onChange={(e) => this._onInputChange(e, "code")}
            errors={this.state.errors.code}
            rows={8}
            cols={textareaColumns}
          />
        </div>
        <div className="mb-3">
          <h5>{i18n.t("snippetCreate.fields.tags")}</h5>

          <Typeahead
            multiple
            id="chooseTags"
            placeholder={i18n.t("snippetCreate.placeholders.tags")}
            options={tags}
            labelKey="name"
            onChange={(selected) => this._onTypeaheadChange(selected, "tags")}
          />
        </div>
        <button
          className={
            "no-margin shadow btn btn-primary btn-lg btn-block mt-2 rounded-border ld-over-inverse form-button " +
            (this.state.loading ? "running" : "")
          }
          type="submit"
        >
          {i18n.t("snippetCreate.button")}
        </button>
        <Alert
          color="danger"
          className="shadow flex-center custom-alert"
          isOpen={this.state.alert.show}
          toggle={() => this.onDismiss()}
        >
          {this.state.alert.message}
        </Alert>
      </form>
    );
  }
}

export default withRouter(SnippetCreateForm);
