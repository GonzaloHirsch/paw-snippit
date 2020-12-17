import React, { Component } from "react";
import { withRouter, matchPath } from "react-router-dom";
import Icon from "@mdi/react";
import i18n from "../../i18n";
import { mdiMagnify } from "@mdi/js";
import { CONTEXT } from "../../js/constants";
import {
  fillNavSearchFromUrl,
  fillTagSearchFromUrl,
  fillLanguageSearchFromUrl,
} from "../../js/search_from_url";

class SearchBar extends Component {
  // Lifecycle hooks
  // -----------------------------------------------------------------------------

  constructor(props) {
    super(props);

    const {
      query,
      type,
      sort,
      showEmpty,
      showOnlyFollowing,
    } = this.props.search;

    this.state = {
      search: {
        query: query === null ? "" : query,
        type: type === null ? "all" : type,
        sort: sort === null ? "no" : sort,
        showEmpty: showEmpty === null ? true : showEmpty,
        showOnlyFollowing:
          showOnlyFollowing === null ? false : showOnlyFollowing,
      },
    };
  }

  // Updating with the url info
  UNSAFE_componentWillReceiveProps(nextProps) {
    const currentSearch = { ...this.state.search };
    const { search } = nextProps;

    currentSearch.query = search.query;
    currentSearch.type = search.type;
    currentSearch.sort = search.sort;
    currentSearch.showEmpty =
      typeof search.showEmpty === "boolean"
        ? search.showEmpty
        : search.showEmpty === "true";
    currentSearch.showOnlyFollowing =
      typeof search.showOnlyFollowing === "boolean"
        ? search.showOnlyFollowing
        : search.showOnlyFollowing === "true";

    this.setState({ search: currentSearch });
  }

  // Events
  // -----------------------------------------------------------------------------

  handleSearch(event, search) {
    if (event !== null) {
      event.preventDefault();
    }
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
    const params = this.fillSearchBasedOnContext(search, this.props.ctx);
    params.set("page", 1);

    // Pushing the route
    this.props.history.push({
      pathname: route,
      search: "?" + params.toString(),
    });
  }

  handleSearchChange(e, name, inversePrevValue) {
    let search = { ...this.state.search };
    search[name] = inversePrevValue ? !search[name] : e.target.value;
    this.setState({ search: search });
  }

  handleSearchChangeAndSearch(e, name, inversePrevValue) {
    let search = { ...this.state.search };
    search[name] = inversePrevValue ? !search[name] : e.target.value;
    this.setState({ search: search }, () => {
      this.handleSearch(null, this.state.search);
    });
  }

  // Adds possible missing params from the url
  fillSearchBasedOnContext(search, ctx) {
    let params = new URLSearchParams(this.props.location.search);
    if (ctx === CONTEXT.TAGS) {
      return fillTagSearchFromUrl(search, params);
    } else if (ctx === CONTEXT.LANGUAGES) {
      return fillLanguageSearchFromUrl(search, params);
    }
    return fillNavSearchFromUrl(search, params);
  }

  // Getters
  // -----------------------------------------------------------------------------

  getShowEmptySearchControl() {
    return (
      <button
        className="btn btn-outline-secondary"
        type="button"
        onClick={(e) => this.handleSearchChangeAndSearch(e, "showEmpty", true)}
      >
        {this.state.search.showEmpty
          ? i18n.t("nav.search.hideEmpty")
          : i18n.t("nav.search.showEmpty")}
      </button>
    );
  }

  getSearchItemsByContext(ctx) {
    if (ctx === CONTEXT.TAGS) {
      return (
        <React.Fragment>
          {this.getShowEmptySearchControl()}
          <button
            className="btn btn-outline-secondary"
            type="button"
            onClick={(e) =>
              this.handleSearchChangeAndSearch(e, "showOnlyFollowing", true)
            }
          >
            {this.state.search.showOnlyFollowing
              ? i18n.t("nav.search.hideOnlyFollowing")
              : i18n.t("nav.search.showOnlyFollowing")}
          </button>
        </React.Fragment>
      );
    } else if (ctx === CONTEXT.LANGUAGES) {
      return (
        <React.Fragment>{this.getShowEmptySearchControl()}</React.Fragment>
      );
    } else {
      return (
        <React.Fragment>
          <select
            className="navbar-dropdowns custom-select form-control"
            id="inputGroupSelect02"
            onChange={(e) => this.handleSearchChange(e, "type", false)}
            value={this.state.search.type}
          >
            <option value="0">{i18n.t("nav.search.filter.hint")}</option>
            <option value="all">{i18n.t("nav.search.filter.all")}</option>
            <option value="tag">{i18n.t("nav.search.filter.tag")}</option>
            <option value="title">{i18n.t("nav.search.filter.title")}</option>
            <option value="content">
              {i18n.t("nav.search.filter.content")}
            </option>
            <option value="username">
              {i18n.t("nav.search.filter.username")}
            </option>
            <option value="language">
              {i18n.t("nav.search.filter.language")}
            </option>
          </select>
          <select
            className="navbar-dropdowns custom-select form-control"
            id="inputGroupSelect03"
            onChange={(e) => this.handleSearchChange(e, "sort", false)}
            value={this.state.search.sort}
          >
            <option value="0">{i18n.t("nav.search.order.hint")}</option>
            <option value="asc">{i18n.t("nav.search.order.ascending")}</option>
            <option value="desc">
              {i18n.t("nav.search.order.descending")}
            </option>
            <option value="no">{i18n.t("nav.search.order.no")}</option>
          </select>
        </React.Fragment>
      );
    }
  }

  render() {
    const { ctx } = this.props;
    return (
      <form
        className="input-group mr-sm-2 search-box"
        onSubmit={(e) => this.handleSearch(e, this.state.search)}
      >
        <input
          type="text"
          className="search-box-style form-control"
          placeholder={i18n.t("nav.search.searchHint")}
          aria-label={i18n.t("nav.search.searchHint")}
          aria-describedby="button-addon2"
          onChange={(e) => this.handleSearchChange(e, "query", false)}
          value={this.state.search.query}
        />
        {this.getSearchItemsByContext(ctx)}
        <div className="input-group-append">
          <button
            className="search-button btn btn-outline-secondary"
            type="submit"
            id="button-addon2"
          >
            <Icon path={mdiMagnify} size={1} />
          </button>
        </div>
      </form>
    );
  }
}

export default withRouter(SearchBar);
