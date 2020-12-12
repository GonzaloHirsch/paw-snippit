import React, { Component } from "react";
import { Typeahead } from "react-bootstrap-typeahead"; // ES2015
import "react-bootstrap-typeahead/css/Typeahead.css";
import SnippetClient from "../../api/implementations/SnippetClient";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import TextAreaInputField from "../forms/text_area_input_field";
import i18n from "../../i18n";

class SnippetCreateForm extends Component {
  snippetClient;
  languagesAndTagsClient;

  constructor(props) {
    super(props);
    this.snippetClient = new SnippetClient(this.props, this.props.token);
    this.languagesAndTagsClient = new LanguagesAndTagsClient();
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
        console.log(this.state.options);
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this._loadLanguages();
    this._loadTags();
  }

  _onTypeaheadChange(selected, key) {
    let fields = { ...this.state.fields };
    fields[key] = selected;
    this.setState({ fields: fields });
  }

  _onInputChange(e, key) {
    let fields = { ...this.state.fields };
    fields[key] = e.target.value;
    this.setState({ fields: fields });
  }

  _onSubmit() {
    console.log(this.state.fields);
  }

  render() {
    const { languages, tags } = this.state.options;
    const { fields, errors } = this.state;
    const textareaColumns = 60;
    return (
      <form className="d-flex flex-column justify-space-between">
        <div className="parent-width d-flex flex-row mb-3">
          <div
            className="d-flex flex-column"
            style={{ width: "65%", marginRight: "30px" }}
          >
            <h5>{"Title"}</h5>
            <input
              id="snippetTitleInput"
              type="text"
              className={"form-control  " + (errors.title && "with-error")}
              placeholder={"Enter title"}
              onChange={(e) => this._onInputChange(e, "title")}
              value={fields.title}
            />
          </div>
          <div className="d-flex flex-column" style={{ width: "30%" }}>
            <h5>Language</h5>
            <Typeahead
              id="chooseLanguage"
              placeholder={"Select a Language"}
              options={languages}
              labelKey="name"
              onChange={(selected) =>
                this._onTypeaheadChange(selected, "language")
              }
              selected={fields.language}
            />
          </div>
        </div>
        <div className="mb-3">
          <h5>Description</h5>
          <TextAreaInputField
            id={"inputDescription"}
            htmlFor={"inputDescription"}
            type={"text"}
            placeholder={"Enter your description"}
            onChange={(e) => this._onInputChange(e, "description")}
            errors={this.state.errors.description}
            rows={4}
            cols={textareaColumns}
          />
        </div>
        <div className="mb-3">
          <h5>Code</h5>
          <TextAreaInputField
            id={"inputDescription"}
            htmlFor={"inputDescription"}
            type={"text"}
            placeholder={"Enter your description"}
            onChange={(e) => this._onInputChange(e, "code")}
            errors={this.state.errors.description}
            rows={8}
            cols={textareaColumns}
          />
        </div>
        <div className="mb-3">
          <h5>Tags</h5>
          <Typeahead
            multiple
            id="chooseTags"
            className="parent-width"
            placeholder={"Select Tags"}
            options={tags}
            labelKey="name"
            onChange={(selected) => this._onTypeaheadChange(selected, "tags")}
          />
        </div>
        <button
          className="mt-2 btn btn-lg btn-primary btn-block rounded-border form-button"
          type="submit"
        >
          CREATE
        </button>{" "}
      </form>
    );
  }
}

export default SnippetCreateForm;
