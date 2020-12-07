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

class ExploreForm extends Component {
  constructor(props) {
    super(props);
    const {
      field,
      sort,
      includeFlagged,
      title,
      language,
      tag,
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
        field: field == null ? "" : field,
        sort: sort == null ? "" : sort,
        includeFlagged: includeFlagged == null ? false : includeFlagged,
        title: title == null ? "" : title,
        language: language == null ? -1 : language,
        tag: tag == null ? -1 : tag,
        username: username == null ? "" : username,
        minRep: minRep == null ? "" : minRep,
        maxRep: maxRep == null ? "" : maxRep,
        minDate: minDate == null ? "" : minDate,
        maxDate: maxDate == null ? "" : maxDate,
        minVotes: minVotes == null ? "" : minVotes,
        maxVotes: maxVotes == null ? "" : maxVotes,
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
      },
    };
  }

  handleSearch() {
    // Determine if we add the "search" to the route
    const isSearching = !!matchPath(this.props.location.pathname, "**/search");
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
      // EXPLORE.MINDATE,
      // EXPLORE.MAXDATE,
      EXPLORE.MINVOTES,
      EXPLORE.MAXVOTES,
    ];
    let params = new URLSearchParams(this.props.location.search);
    queryFields.forEach((field) => {
      this._setQueryParam(params, field);
    });

    this._setParamWithDefault(params, EXPLORE.FIELD, "date");
    this._setParamWithDefault(params, EXPLORE.SORT, "desc");
    this._setParamWithDefault(params, EXPLORE.FLAGGED, true); // FIXME!
    this._setParamWithDefault(params, EXPLORE.LANGUAGE, -1);
    this._setParamWithDefault(params, EXPLORE.TAG, -1);

    // Pushing the route
    this.props.history.push({
      pathname: route,
      search: "?" + params.toString(),
    });
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
    if (value !== null && value !== undefined && value != "") {
      params.set(name, value);
    } else {
      params.set(name, defaultValue);
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

  _rangeErrors(key) {
    let keyErrors = this.state.errors[key];
    return (
      <div className="d-flex flex-column mb-1">
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

  onChange = (key, e) => {
    let fields = this.state.fields;
    fields[key] = e.target.value;
    this.setState({ fields: fields });
  };

  onChangeMinValidation = (minFieldKey, maxFieldKey, errorKey, e) => {
    this.onChange(minFieldKey, e);
    this.validateIntervals(minFieldKey, maxFieldKey, errorKey);
  };

  onChangeMaxValidation = (minFieldKey, maxFieldKey, errorKey, e) => {
    this.onChange(maxFieldKey, e);
    this.validateIntervals(minFieldKey, maxFieldKey, errorKey);
  };

  validateIntervals = (minFieldKey, maxFieldKey, errorKey) => {
    const errors = this.state.errors;
    let fields = this.state.fields;
    let minValue = parseInt(fields[minFieldKey]);
    let maxValue = parseInt(fields[maxFieldKey]);

    if (
      (minValue != "" || minValue == 0) &&
      (maxValue != "" || maxValue == 0)
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
    console.log(errors);
    this.setState({ errors: errors });
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

    return (
      <form className="flex-column" onSubmit={() => this.handleSearch()}>
        <h6>{i18n.t(orderPrefix + "header")}</h6>
        <div className="d-flex flex-row">
          <DropdownMenu
            id={"exploreOrderByMenu"}
            value={this.state.fields.field}
            options={this._getOrderOptions(EXPLORE_ORDERBY, orderPrefix)}
            defaultValue={""}
            description={i18n.t(orderPrefix + "placeholder")}
            onChange={(e) => this.onChange(EXPLORE.FIELD, e)}
          />
          <div className="m-2"></div>
          <DropdownMenu
            id={"exploreSortMenu"}
            value={this.state.fields.sort}
            description={i18n.t(sortPrefix + "placeholder")}
            defaultValue={""}
            options={this._getOrderOptions(SORT, sortPrefix)}
            onChange={(e) => this.onChange(EXPLORE.SORT, e)}
          />
        </div>
        <hr />
        <h6>{i18n.t(titlePrefix + "header")}</h6>
        <InputField
          value={this.state.fields.title}
          placeholder={i18n.t(titlePrefix + "placeholder")}
          onChange={(e) => this.onChange(EXPLORE.TITLE, e)}
        />
        <hr />
        <div className="d-flex flex-row">
          <div>
            <h6>{i18n.t(languagePrefix + "header")}</h6>
            <DropdownMenu
              id={"exploreLanguageMenu"}
              value={this.state.fields.language}
              description={i18n.t(languagePrefix + "placeholder")}
              defaultValue={-1}
              options={this.props.languages}
              onChange={(e) => this.onChange(EXPLORE.LANGUAGE, e)}
            />
          </div>
          <div className="m-2"></div>
          <div>
            <h6>{i18n.t(tagPrefix + "header")}</h6>
            <DropdownMenu
              id={"exploreTagMenu"}
              value={this.state.fields.tag}
              description={i18n.t(tagPrefix + "placeholder")}
              defaultValue={-1}
              options={this.props.tags}
              onChange={(e) => this.onChange(EXPLORE.TAG, e)}
            />
          </div>
        </div>
        <hr />
        <h6>{i18n.t(userPrefix + "username")}</h6>
        <InputField
          value={this.state.fields.username}
          placeholder={i18n.t(userPrefix + "username")}
          onChange={(e) => this.onChange(EXPLORE.USERNAME, e)}
        />
        <hr />
        <h6>{i18n.t(userPrefix + "reputation")}</h6>
        <div className="d-flex flex-column">
          {this._rangeErrors(repErrorKey)}
          <div className="d-flex flex-row">
            <InputField
              type={"number"}
              value={this.state.fields.minRep}
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
              value={this.state.fields.maxRep}
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
              value={this.state.fields.minVotes}
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
              value={this.state.fields.maxVotes}
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
        <hr />
        <h6>{i18n.t(flaggedPrefix + "header")}</h6>
        <div className="d-flex flex-row">
          <CustomCheckbox
            label={i18n.t(flaggedPrefix + "placeholder")}
            value={this.state.fields.includeFlagged} //FIXME!
            onChange={(e) => this.onChange(EXPLORE.FLAGGED, e)}
          />
        </div>
        <hr />
        <button
          className="btn btn-lg btn-primary btn-block rounded-border form-button"
          type="submit"
        >
          {i18n.t("explore.form.submit")}
        </button>
      </form>
    );
  }
}

export default withRouter(ExploreForm);
