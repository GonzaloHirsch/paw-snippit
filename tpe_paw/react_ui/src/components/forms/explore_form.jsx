import React, { Component } from "react";
import { matchPath, withRouter } from "react-router-dom";
import InputField from "../forms/input_field";
import i18n from "../../i18n";
import {
  EXPLORE,
  EXPLORE_ORDERBY,
  SORT,
  MIN_INTEGER,
  MAX_INTEGER,
} from "../../js/constants";
import DropdownMenu from "../forms/dropdown_menu";
import CustomCheckbox from "../forms/custom_checkbox";
import { EXPLORE_FORM_VALIDATIONS } from "../../js/validations";
import CustomDatePicker from "../forms/date_picker";
import { getDateFromString } from "../../js/date_utils";
import { Typeahead } from "react-bootstrap-typeahead"; // ES2015
import "react-bootstrap-typeahead/css/Typeahead.css";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";

class ExploreForm extends Component {
  languagesAndTagsClient;

  constructor(props) {
    super(props);

    this.languagesAndTagsClient = new LanguagesAndTagsClient(this.props);

    const {
      field,
      sort,
      includeFlagged,
      title,
      username,
      minRep,
      maxRep,
      minDate,
      maxDate,
      minVotes,
      maxVotes,
    } = this.props.urlSearch;

    this.state = {
      fields: {
        field: field === null ? "" : field,
        sort: sort === null ? "" : sort,
        includeFlagged: includeFlagged === null ? false : includeFlagged,
        title: title === null ? "" : title,
        language: [],
        tag: [],
        username: username === null ? "" : username,
        minRep: minRep === null ? "" : minRep,
        maxRep: maxRep === null ? "" : maxRep,
        minDate: getDateFromString(minDate),
        maxDate: getDateFromString(maxDate),
        minVotes: minVotes === null ? "" : minVotes,
        maxVotes: maxVotes === null ? "" : maxVotes,
      },
      errors: {
        userReputation: {
          range: null,
          min: null,
          max: null,
        },
        snippetVotes: {
          range: null,
          min: null,
          max: null,
        },
        dateUploaded: null,
        title: null,
        username: null,
      },
      options: {
        languages: [],
        tags: [],
      },
    };
  }

  loadLanguages() {
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

  loadLanguage(language) {
    if (language === null || parseInt(language) === -1) return;
    this.languagesAndTagsClient
      .getLanguageWithId(language)
      .then((res) => {
        let fields = { ...this.state.fields };
        fields.language = [res.data];
        this.setState({
          fields: fields,
        });
      })
      .catch((e) => {});
  }

  loadTags() {
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

  loadTag(tag) {
    if (tag === null || parseInt(tag) === -1) return;
    this.languagesAndTagsClient
      .getTagWithId(tag)
      .then((res) => {
        let fields = { ...this.state.fields };
        fields.tag = [res.data];
        this.setState({
          fields: fields,
        });
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this.loadLanguage(this.props.urlSearch.language);
    this.loadTag(this.props.urlSearch.tag);
    this.loadLanguages();
    this.loadTags();
    this.validateAll();
  }

  /* Upon URL change, the fields in the form will update */
  componentWillReceiveProps(nextProps) {
    const fields = { ...this.state.fields };
    const { urlSearch } = nextProps;

    // First time, everything comes as null and dont want to save those values
    if (urlSearch.title !== null) {
      if (urlSearch.language === -1) {
        fields[EXPLORE.LANGUAGE] = [];
      } else if (
        (fields.language.length === 0 && urlSearch.language !== -1) ||
        (fields.language.length > 0 &&
          fields.language[0].id !== urlSearch.language)
      ) {
        this.loadLanguage(urlSearch.language);
      }

      if (urlSearch.tag === -1) {
        fields[EXPLORE.TAG] = [];
      } else if (
        (fields.tag.length === 0 && urlSearch.tag !== -1) ||
        (fields.tag.length > 0 && fields.tag[0].id !== urlSearch.tag)
      ) {
        this.loadTag(urlSearch.tag);
      }

      fields[EXPLORE.FIELD] = urlSearch.field;
      fields[EXPLORE.SORT] = urlSearch.sort;
      fields[EXPLORE.FLAGGED] = urlSearch.includeFlagged;
      fields[EXPLORE.TITLE] = urlSearch.title;
      fields[EXPLORE.USERNAME] = urlSearch.username;
      fields[EXPLORE.MINREP] = urlSearch.minRep;
      fields[EXPLORE.MAXREP] = urlSearch.maxRep;
      fields[EXPLORE.MINDATE] = getDateFromString(urlSearch.minDate);
      fields[EXPLORE.MAXDATE] = getDateFromString(urlSearch.maxDate);
      fields[EXPLORE.MINVOTES] = urlSearch.minVotes;
      fields[EXPLORE.MAXVOTES] = urlSearch.maxVotes;

      this.setState({ fields: fields });
      this.validateAll();
    }
  }

  handleSearch() {
    if (this.validateAll()) {
      // Determine if we add the "search" to the route
      const isSearching = !!matchPath(
        this.props.location.pathname,
        "**/search"
      );
      let route;
      if (isSearching) {
        route = this.props.location.pathname;
      } else {
        let toAdd = "search";
        if (
          !(
            this.props.location.pathname.charAt(
              this.props.location.pathname.length - 1
            ) === "/"
          )
        ) {
          toAdd = "/search";
        }
        route = this.props.location.pathname + toAdd;
      }

      // Adding the params to not lose the existing ones
      const queryFields = [
        EXPLORE.TITLE,
        EXPLORE.USERNAME,
        EXPLORE.MINREP,
        EXPLORE.MAXREP,
        EXPLORE.MINVOTES,
        EXPLORE.MAXVOTES,
      ];
      let params = new URLSearchParams(this.props.location.search);
      queryFields.forEach((field) => {
        this._setQueryParam(params, field);
      });

      this._setDateParam(params, EXPLORE.MINDATE, "");
      this._setDateParam(params, EXPLORE.MAXDATE, "");
      this._setParamWithDefault(params, EXPLORE.FIELD, "date");
      this._setParamWithDefault(params, EXPLORE.FIELD, "date");
      this._setParamWithDefault(params, EXPLORE.SORT, "desc");
      this._setParamWithDefault(params, EXPLORE.FLAGGED, true); // FIXME!
      this._setTypeaheadWithDefault(params, EXPLORE.LANGUAGE);
      this._setTypeaheadWithDefault(params, EXPLORE.TAG);

      // Pushing the route
      this.props.history.push({
        pathname: route,
        search: "?" + params.toString(),
      });
    }
  }

  _setDateParam(params, name) {
    const value = this.state.fields[name];
    if (value !== null && value !== undefined) {
      params.set(name, value.toISOString().split("T")[0]);
    } else {
      params.set(name, "");
    }
  }

  _setQueryParam(params, name) {
    const value = this.state.fields[name];
    if (value !== null && value !== undefined) {
      params.set(name, value);
    } else {
      params.set(name, "");
    }
  }
  _setParamWithDefault(params, name, defaultValue) {
    const value = this.state.fields[name];
    if (value !== null && value !== undefined && value !== "") {
      params.set(name, value);
    } else {
      params.set(name, defaultValue);
    }
  }

  _setTypeaheadWithDefault(params, name) {
    const value = this.state.fields[name];

    if (value.length > 0) {
      params.set(name, value[0].id);
    } else {
      params.set(name, -1);
    }
  }

  _getOrderOptions(items, prefix) {
    const options = [];

    items.forEach((item) => {
      options.push({
        id: item,
        name: i18n.t(prefix + item),
      });
    });
    return options;
  }

  _resetFilters() {
    const newState = {
      fields: {
        field: "",
        sort: "",
        includeFlagged: false,
        title: "",
        language: [],
        tag: [],
        username: "",
        minRep: "",
        maxRep: "",
        minDate: null,
        maxDate: null,
        minVotes: "",
        maxVotes: "",
      },
      errors: {
        userReputation: {
          range: null,
          min: null,
          max: null,
        },
        snippetVotes: {
          range: null,
          min: null,
          max: null,
        },
        dateUploaded: null,
        title: null,
        username: null,
      },
    };
    this.setState(newState);
  }

  // Validations and error handling
  _rangeErrors(key) {
    let keyErrors = this.state.errors[key];
    return (
      <div className="d-flex flex-column">
        {keyErrors.range && (
          <span className="text-danger">{keyErrors.range}</span>
        )}{" "}
        {keyErrors.min && <span className="text-danger">{keyErrors.min}</span>}{" "}
        {keyErrors.max && <span className="text-danger">{keyErrors.max}</span>}
      </div>
    );
  }

  _rangeHasErrors(key) {
    let keyErrors = this.state.errors[key];
    return (
      keyErrors.range != null || keyErrors.min != null || keyErrors.max != null
    );
  }

  _inputHasErrors(key) {
    return this.state.errors[key] != null;
  }

  validateInput = (key) => {
    const errors = this.state.errors;
    errors[key] = EXPLORE_FORM_VALIDATIONS[key](this.state.fields[key]);
    this.setState({ errors: errors });
  };

  validateIntervals = (minFieldKey, maxFieldKey, errorKey) => {
    const errors = this.state.errors;
    const fields = this.state.fields;
    const minValue = parseInt(fields[minFieldKey]);
    const maxValue = parseInt(fields[maxFieldKey]);

    if (
      (minValue !== "" || minValue === 0) &&
      (maxValue !== "" || maxValue === 0)
    ) {
      errors[errorKey].range =
        minValue > maxValue ? i18n.t("explore.form.errors.range") : null;
    } else {
      errors[errorKey].range = null;
    }

    errors[errorKey].min =
      minValue < MIN_INTEGER || maxValue < MIN_INTEGER
        ? (errors[errorKey].min = i18n.t("explore.form.errors.min", {
            min: MIN_INTEGER,
          }))
        : null;

    errors[errorKey].max =
      minValue > MAX_INTEGER || maxValue > MAX_INTEGER
        ? (errors[errorKey].max = i18n.t("explore.form.errors.max", {
            max: MAX_INTEGER,
          }))
        : null;
    this.setState({ errors: errors });
  };

  validateAll() {
    // Function will return true if the values are correct
    const repErrorKey = "userReputation";
    const snippetErrorKey = "snippetVotes";

    let hasErrors = false;
    this.validateIntervals(EXPLORE.MINREP, EXPLORE.MAXREP, repErrorKey);
    this.validateIntervals(EXPLORE.MINVOTES, EXPLORE.MAXVOTES, snippetErrorKey);
    this.validateInput(EXPLORE.TITLE);
    this.validateInput(EXPLORE.USERNAME);

    hasErrors = hasErrors || this._inputHasErrors(EXPLORE.TITLE);
    hasErrors = hasErrors || this._inputHasErrors(EXPLORE.USERNAME);
    hasErrors = hasErrors || this._rangeHasErrors("userReputation");
    hasErrors = hasErrors || this._rangeHasErrors("snippetVotes");

    return !hasErrors;
  }

  // Handlers
  onChange = (key, e, useChecked) => {
    let fields = this.state.fields;
    let errors = this.state.errors;
    fields[key] = useChecked ? e.target.checked : e.target.value;
    if (key in errors) {
      errors[key] = EXPLORE_FORM_VALIDATIONS[key](fields[key]);
    }
    this.setState({ fields: fields, errors: errors });
  };

  _onTypeaheadChange(name, selected) {
    let fields = { ...this.state.fields };
    fields[name] = selected;
    this.setState({ fields: fields });
  }

  onDateChange = (key, e) => {
    let fields = this.state.fields;
    let errors = this.state.errors;
    fields[key] = e;

    if (key in errors) {
      errors[key] = EXPLORE_FORM_VALIDATIONS[key](fields[key]);
    }
    this.setState({ fields: fields, errors: errors });
  };

  onChangeMinValidation = (minFieldKey, maxFieldKey, errorKey, e) => {
    this.onChange(minFieldKey, e, false);
    this.validateIntervals(minFieldKey, maxFieldKey, errorKey);
  };

  onChangeMaxValidation = (minFieldKey, maxFieldKey, errorKey, e) => {
    this.onChange(maxFieldKey, e, false);
    this.validateIntervals(minFieldKey, maxFieldKey, errorKey);
  };

  render() {
    const orderPrefix = "explore.form.orderBy.";
    const sortPrefix = "explore.form.sort.";
    const languagePrefix = "explore.form.language.";
    const tagPrefix = "explore.form.tags.";
    const userPrefix = "explore.form.user.";
    const titlePrefix = "explore.form.title.";
    const placeholderPrefix = "explore.form.placeholder.";
    const flaggedPrefix = "explore.form.flagged.";
    const repErrorKey = "userReputation";
    const snippetErrorKey = "snippetVotes";

    const { fields, errors, options } = this.state;

    return (
      <form className="flex-column" onSubmit={() => this.handleSearch()}>
        <h6>{i18n.t(orderPrefix + "header")}</h6>
        <div className="d-flex flex-row">
          <DropdownMenu
            id={"exploreOrderByMenu"}
            value={fields.field}
            options={this._getOrderOptions(EXPLORE_ORDERBY, orderPrefix)}
            defaultValue={""}
            description={i18n.t(orderPrefix + "placeholder")}
            onChange={(e) => this.onChange(EXPLORE.FIELD, e, false)}
          />
          <div className="m-2"></div>
          <DropdownMenu
            id={"exploreSortMenu"}
            value={fields.sort}
            description={i18n.t(sortPrefix + "placeholder")}
            defaultValue={""}
            options={this._getOrderOptions(SORT, sortPrefix)}
            onChange={(e) => this.onChange(EXPLORE.SORT, e, false)}
          />
        </div>
        <hr />
        <h6>{i18n.t(titlePrefix + "header")}</h6>
        {errors.title && <span className="text-danger">{errors.title}</span>}
        <InputField
          value={fields.title}
          placeholder={i18n.t(titlePrefix + "placeholder")}
          onChange={(e) => this.onChange(EXPLORE.TITLE, e, false)}
          error={this._inputHasErrors(EXPLORE.TITLE)}
        />
        <hr />
        <div className="d-flex flex-row">
          <div>
            <h6>{i18n.t(languagePrefix + "header")}</h6>
            <Typeahead
              id={"exploreLanguageMenu"}
              placeholder={i18n.t(languagePrefix + "placeholder")}
              options={options.languages}
              labelKey="name"
              onChange={(selected) =>
                this._onTypeaheadChange(EXPLORE.LANGUAGE, selected)
              }
              selected={fields.language}
            />
          </div>
          <div className="m-2"></div>
          <div>
            <h6>{i18n.t(tagPrefix + "header")}</h6>
            <Typeahead
              id={"exploreTagMenu"}
              placeholder={i18n.t(tagPrefix + "placeholder")}
              options={options.tags}
              labelKey="name"
              onChange={(e) => this._onTypeaheadChange(EXPLORE.TAG, e)}
              selected={fields.tag}
            />
          </div>
        </div>
        <hr />
        <h6>{i18n.t(userPrefix + "username")}</h6>
        {errors.username && (
          <span className="text-danger">{errors.username}</span>
        )}
        <InputField
          value={fields.username}
          placeholder={i18n.t(userPrefix + "username")}
          onChange={(e) => this.onChange(EXPLORE.USERNAME, e, false)}
          error={this._inputHasErrors(EXPLORE.USERNAME)}
        />
        <hr />
        <h6>{i18n.t(userPrefix + "reputation")}</h6>
        <div className="d-flex flex-column">
          {this._rangeErrors(repErrorKey)}
          <div className="d-flex flex-row">
            <InputField
              type={"number"}
              value={fields.minRep}
              placeholder={i18n.t(placeholderPrefix + "from")}
              onChange={(e) =>
                this.onChangeMinValidation(
                  EXPLORE.MINREP,
                  EXPLORE.MAXREP,
                  repErrorKey,
                  e
                )
              }
              error={this._rangeHasErrors(repErrorKey)}
            />
            <div className="m-2"></div>
            <InputField
              type={"number"}
              value={fields.maxRep}
              placeholder={i18n.t(placeholderPrefix + "to")}
              onChange={(e) =>
                this.onChangeMaxValidation(
                  EXPLORE.MINREP,
                  EXPLORE.MAXREP,
                  repErrorKey,
                  e
                )
              }
              error={this._rangeHasErrors(repErrorKey)}
            />
          </div>
        </div>
        <hr />
        <h6>{i18n.t("explore.form.votes")}</h6>
        <div className="d-flex flex-column">
          {this._rangeErrors(snippetErrorKey)}
          <div className="d-flex flex-row">
            {" "}
            <InputField
              type={"number"}
              value={fields.minVotes}
              placeholder={i18n.t(placeholderPrefix + "from")}
              onChange={(e) =>
                this.onChangeMinValidation(
                  EXPLORE.MINVOTES,
                  EXPLORE.MAXVOTES,
                  snippetErrorKey,
                  e
                )
              }
              error={this._rangeHasErrors(snippetErrorKey)}
            />
            <div className="m-2"></div>
            <InputField
              type={"number"}
              value={fields.maxVotes}
              placeholder={i18n.t(placeholderPrefix + "to")}
              onChange={(e) =>
                this.onChangeMaxValidation(
                  EXPLORE.MINVOTES,
                  EXPLORE.MAXVOTES,
                  snippetErrorKey,
                  e
                )
              }
              error={this._rangeHasErrors(snippetErrorKey)}
            />
          </div>
        </div>
        <hr />
        <h6>{i18n.t("explore.form.date")}</h6>
        <div className="d-flex flex-row">
          <CustomDatePicker
            date={fields.minDate}
            onChange={(e) => this.onDateChange(EXPLORE.MINDATE, e)}
            placeholder={i18n.t(placeholderPrefix + "from")}
          />
          <div className="m-2"></div>
          <CustomDatePicker
            date={fields.maxDate}
            onChange={(e) => this.onDateChange(EXPLORE.MAXDATE, e)}
            placeholder={i18n.t(placeholderPrefix + "to")}
          />
        </div>
        <hr />
        <h6>{i18n.t(flaggedPrefix + "header")}</h6>
        <div className="d-flex flex-row">
          <CustomCheckbox
            label={i18n.t(flaggedPrefix + "placeholder")}
            onChange={(e) => this.onChange(EXPLORE.FLAGGED, e, true)}
          />
        </div>
        <hr />
        <div className="d-flex flex-row">
          <div
            className="mt-2 btn btn-lg btn-primary btn-block rounded-border form-button"
            onClick={() => this._resetFilters()}
            style={{ width: "35%" }}
          >
            {i18n.t("explore.form.reset")}
          </div>
          <div className="m-2"></div>
          <button
            className="mt-2 btn btn-lg btn-primary btn-block rounded-border form-button"
            type="submit"
          >
            {i18n.t("explore.form.submit")}
          </button>
        </div>
      </form>
    );
  }
}

export default withRouter(ExploreForm);
