import React, { Component } from "react";
import { Typeahead } from "react-bootstrap-typeahead"; // ES2015
import "react-bootstrap-typeahead/css/Typeahead.css";
import SnippetClient from "../../api/implementations/SnippetClient";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import TextAreaInputField from "../forms/text_area_input_field";

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
    console.log(selected);
    let fields = { ...this.state.fields };
    fields[key] = selected;
    this.setState({ fields: fields });
    console.log(fields.tags);
  }

  render() {
    const { languages, tags } = this.state.options;
    const textareaColumns = 60;
    return (
      <form className="d-flex flex-column justify-space-between">
        <div className="parent-width d-flex flex-row">
          <div
            className="d-flex flex-column"
            style={{ width: "65%", marginRight: "30px" }}
          >
            <h4>Title</h4>
            <input type="text"></input>
          </div>
          <div className="d-flex flex-column" style={{ width: "30%" }}>
            <h4>Language</h4>
            <Typeahead
              id="chooseLanguage"
              className="rounded-border"
              placeholder={"Select a Language"}
              options={languages}
              labelKey="name"
              onChange={(selected) =>
                this._onTypeaheadChange(selected, "language")
              }
              selected={this.state.fields.language}
            />
          </div>
        </div>
        <div>
          <h4>Description</h4>
          <TextAreaInputField
            id={"inputDescription"}
            htmlFor={"inputDescription"}
            type={"text"}
            placeholder={"Enter your description"}
            onChange={(e) => console.log("HEY")}
            errors={this.state.errors.description}
            rows={4}
            cols={textareaColumns}
          />
        </div>
        <div>
          <h4>Code</h4>
          <TextAreaInputField
            id={"inputDescription"}
            htmlFor={"inputDescription"}
            type={"text"}
            placeholder={"Enter your description"}
            onChange={(e) => console.log("HEY")}
            errors={this.state.errors.description}
            rows={10}
            cols={textareaColumns}
          />
        </div>
        <div>
          <h4>Tags</h4>
          <Typeahead
            multiple
            id="chooseTags"
            className="rounded-border parent-width"
            placeholder={"Select Tags"}
            options={tags}
            labelKey="name"
            onChange={(selected) => this._onTypeaheadChange(selected, "tags")}
          />
        </div>
        <button type="submit">CREATE</button>
      </form>
    );
  }
}

export default SnippetCreateForm;
