import React, { Component } from "react";
import { matchPath, withRouter } from "react-router-dom";
import InputField from "../forms/input_field";
import i18n from "../../i18n";
import { EXPLORE, EXPLORE_ORDERBY, SORT } from "../../js/constants";
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
      field: field,
      sort: sort,
      includeFlagged: includeFlagged,
      title: title,
      language: language,
      tag: tag,
      username: username,
      minRep: minRep,
      maxRep: maxRep,
      minDate: minDate,
      maxDate: maxDate,
      minVotes: minVotes,
      maxVotes: maxVotes,
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
      this.setQueryParam(params, field);
    });

    this.setParamWithDefault(params, EXPLORE.FIELD, "date");
    this.setParamWithDefault(params, EXPLORE.SORT, "asc");
    this.setParamWithDefault(params, EXPLORE.FLAGGED, true); // FIXME!
    this.setParamWithDefault(params, EXPLORE.LANGUAGE, -1); // TODO: Ver que con el value != "0" ande bien
    this.setParamWithDefault(params, EXPLORE.TAG, -1);

    // Pushing the route
    this.props.history.push({
      pathname: route,
      search: "?" + params.toString(),
    });
  }

  setQueryParam(params, name) {
    const value = this.state[name];
    if (value !== null && value !== undefined) {
      params.set(name, value);
    } else {
      params.set(name, "");
    }
  }
  setParamWithDefault(params, name, defaultValue) {
    const value = this.state[name];
    if (value !== null && value !== undefined && value != "") {
      params.set(name, value);
    } else {
      params.set(name, defaultValue);
    }
  }

  getOrderOptions(items, prefix) {
    const options = [];

    items.forEach((item) => {
      options.push({
        id: item,
        name: i18n.t(prefix + item),
      });
    });
    return options;
  }

  onChange = (key, e) => {
    let o = {};
    o[key] = e.target.value;
    this.setState(o);
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

    return (
      <form className="flex-column" onSubmit={() => this.handleSearch()}>
        <h6>{i18n.t(orderPrefix + "header")}</h6>
        <div className="d-flex flex-row">
          <DropdownMenu
            id={"exploreOrderByMenu"}
            value={this.state.field}
            options={this.getOrderOptions(EXPLORE_ORDERBY, orderPrefix)}
            defaultValue={""}
            description={i18n.t(orderPrefix + "placeholder")}
            onChange={(e) => this.onChange(EXPLORE.FIELD, e)}
          />
          <div className="m-2"></div>
          <DropdownMenu
            id={"exploreSortMenu"}
            value={this.state.sort}
            description={i18n.t(sortPrefix + "placeholder")}
            defaultValue={""}
            options={this.getOrderOptions(SORT, sortPrefix)}
            onChange={(e) => this.onChange(EXPLORE.SORT, e)}
          />
        </div>
        <hr />
        <h6>{i18n.t(titlePrefix + "header")}</h6>
        <InputField
          value={this.state.title}
          placeholder={i18n.t(titlePrefix + "placeholder")}
          onChange={(e) => this.onChange(EXPLORE.TITLE, e)}
        />
        <hr />
        <div className="d-flex flex-row">
          <div>
            <h6>{i18n.t(languagePrefix + "header")}</h6>
            <DropdownMenu
              id={"exploreLanguageMenu"}
              value={this.state.language}
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
              value={this.state.tag}
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
          value={this.state.username}
          placeholder={i18n.t(userPrefix + "username")}
          onChange={(e) => this.onChange(EXPLORE.USERNAME, e)}
        />
        <hr />
        <h6>{i18n.t(userPrefix + "reputation")}</h6>
        <div className="d-flex flex-row">
          <InputField
            type={"number"}
            value={this.state.minRep}
            placeholder={i18n.t(placeholderPrefix + "from")}
            onChange={(e) => this.onChange(EXPLORE.MINREP, e)}
          />
          <div className="m-2"></div>
          <InputField
            type={"number"}
            value={this.state.maxRep}
            placeholder={i18n.t(placeholderPrefix + "to")}
            onChange={(e) => this.onChange(EXPLORE.MAXREP, e)}
          />
        </div>
        <hr />
        <h6>{i18n.t("explore.form.votes")}</h6>
        <div className="d-flex flex-row">
          <InputField
            type={"number"}
            value={this.state.minVotes}
            placeholder={i18n.t(placeholderPrefix + "from")}
            onChange={(e) => this.onChange(EXPLORE.MINVOTES, e)}
          />
          <div className="m-2"></div>
          <InputField
            type={"number"}
            value={this.state.maxVotes}
            placeholder={i18n.t(placeholderPrefix + "to")}
            onChange={(e) => this.onChange(EXPLORE.MAXVOTES, e)}
          />
        </div>
        <hr />
        <h6>{i18n.t("explore.form.date")}</h6>
        <hr />
        <h6>{i18n.t(flaggedPrefix + "header")}</h6>
        <div className="d-flex flex-row">
          <CustomCheckbox
            label={i18n.t(flaggedPrefix + "placeholder")}
            value={this.state.includeFlagged} //FIXME!
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
