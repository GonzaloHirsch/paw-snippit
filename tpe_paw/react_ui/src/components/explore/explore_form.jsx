import React, { Component } from "react";
import { matchPath, withRouter } from "react-router-dom";
import TextInputField from "../general/text_input_field";

{
  /* <form
  className="form-inline my-auto my-lg-0 col-8"
  onSubmit={() => this.handleSearch(this.state.search)}
>
  <div className="input-group mr-sm-2 search-box">
    <input
      type="text"
      className="form-control"
      placeholder={i18n.t("nav.searchHint")}
      aria-label={i18n.t("nav.searchHint")}
      aria-describedby="button-addon2"
      onChange={(e) =>
        this.setState({
          search: {
            query: e.target.value,
            type: this.state.search.type,
            sort: this.state.search.sort,
          },
        })
      }
      value={this.state.search.query}
    />
    <div className="input-group-append">
      <select
        className="custom-select form-control"
        id="inputGroupSelect02"
        onChange={(e) =>
          this.setState({
            search: {
              query: this.state.search.query,
              type: e.target.value,
              sort: this.state.search.sort,
            },
          })
        }
        value={this.state.search.type}
      >
        <option value="0">{i18n.t("nav.filter.hint")}</option>
        <option value="all">{i18n.t("nav.filter.all")}</option>
        <option value="tag">{i18n.t("nav.filter.tag")}</option>
        <option value="title">{i18n.t("nav.filter.title")}</option>
        <option value="content">{i18n.t("nav.filter.content")}</option>
        <option value="username">{i18n.t("nav.filter.username")}</option>
        <option value="language">{i18n.t("nav.filter.language")}</option>
      </select>
      <select
        className="custom-select form-control"
        id="inputGroupSelect03"
        onChange={(e) =>
          this.setState({
            search: {
              query: this.state.search.query,
              type: this.state.search.type,
              sort: e.target.value,
            },
          })
        }
        value={this.state.search.sort}
      >
        <option value="0">{i18n.t("nav.order.hint")}</option>
        <option value="asc">{i18n.t("nav.order.ascending")}</option>
        <option value="desc">{i18n.t("nav.order.descending")}</option>
        <option value="no">{i18n.t("nav.order.no")}</option>
      </select>
      <button
        className="btn btn-outline-secondary"
        type="submit"
        id="button-addon2"
      >
        <Icon path={mdiMagnify} size={1} />
      </button>
    </div>
  </div>
</form>; */
}

class ExploreForm extends Component {
  state = {
    field: "date",
    sort: "no",
    includeFlagged: false,
    title: "",
    language: -1,
    tag: -1,
    username: "",
    minRep: "",
    maxRep: "",
    minDate: "",
    maxDate: "",
    minVotes: "",
    maxVotes: "",
  };

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
      "title",
      "username",
      "minRep",
      "maxRep",
      // "minDate",
      // "maxDate",
      "minVotes",
      "maxVotes",
    ];
    let params = new URLSearchParams(this.props.location.search);
    queryFields.forEach((field) => {
      this.setQueryParam(params, field);
    });

    this.setParamWithDefault(params, "field", "date");
    this.setParamWithDefault(params, "sort", "asc");
    this.setParamWithDefault(params, "includeFlagged", true); // FIXME!
    this.setParamWithDefault(params, "language", -1);
    this.setParamWithDefault(params, "tag", -1);

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
    if (value !== null && value !== undefined && value != "0") {
      params.set(name, value);
    } else {
      params.set(name, defaultValue);
    }
  }

  onChange = (key, e) => {
    let o = {};
    o[key] = e.target.value;
    this.setState(o);
  };

  render() {
    return (
      <form className="flex-column" onSubmit={() => this.handleSearch()}>
        <TextInputField
          value={this.state.title}
          placeholder={"PLACEHOLDER"}
          onChange={(e) => this.onChange("title", e)}
        />
        <TextInputField
          value={this.state.username}
          placeholder={"USER"}
          onChange={(e) => this.onChange("username", e)}
        />
        <button type="submit">SUBMIT</button>
      </form>
    );
  }
}

export default withRouter(ExploreForm);
