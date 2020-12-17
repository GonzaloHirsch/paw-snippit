export function getExploreSearchFromUrl(url) {
  const params = new URLSearchParams(url);
  let search = {};
  search.field = params.get("field");
  search.sort = params.get("sort");
  search.includeFlagged = params.get("includeFlagged");
  search.title = params.get("title");
  search.language = params.get("language");
  search.tag = params.get("tag");
  search.username = params.get("username");
  search.minRep = params.get("minRep");
  search.maxRep = params.get("maxRep");
  search.minDate = params.get("minDate");
  search.maxDate = params.get("maxDate");
  search.minVotes = params.get("minVotes");
  search.maxVotes = params.get("maxVotes");
  return search;
}

export function getNavSearchFromUrl(url) {
  const params = new URLSearchParams(url);
  let search = {};
  search.query = params.get("query");
  search.type = params.get("type");
  search.sort = params.get("sort");
  return search;
}

export function getTagsSearchFromUrl(url) {
  const params = new URLSearchParams(url);
  let search = {};
  search.query = params.get("query");
  search.showEmpty = params.get("showEmpty");
  search.showOnlyFollowing = params.get("showOnlyFollowing");
  return search;
}

export function getLanguagesSearchFromUrl(url) {
  const params = new URLSearchParams(url);
  let search = {};
  search.query = params.get("query");
  search.showEmpty = params.get("showEmpty");
  return search;
}

export function fillNavSearchFromUrl(search, params) {
  if (search.query !== null && search.query !== undefined) {
    params.set("query", search.query);
  } else {
    params.set("query", "");
  }
  if (
    search.type !== null &&
    search.type !== undefined &&
    search.type !== "0"
  ) {
    params.set("type", search.type);
  } else {
    params.set("type", "all");
  }
  if (
    search.sort !== null &&
    search.sort !== undefined &&
    search.sort !== "0"
  ) {
    params.set("sort", search.sort);
  } else {
    params.set("sort", "no");
  }
  return params;
}

export function fillTagSearchFromUrl(search, params) {
  if (search.query !== null && search.query !== undefined) {
    params.set("query", search.query);
  } else {
    params.set("query", "");
  }
  if (search.showEmpty !== null && search.showEmpty !== undefined) {
    params.set("showEmpty", search.showEmpty);
  } else {
    params.set("showEmpty", true);
  }
  if (
    search.showOnlyFollowing !== null &&
    search.showOnlyFollowing !== undefined
  ) {
    params.set("showOnlyFollowing", search.showOnlyFollowing);
  } else {
    params.set("showOnlyFollowing", false);
  }
  return params;
}

export function fillLanguageSearchFromUrl(search, params) {
  if (search.query !== null && search.query !== undefined) {
    params.set("query", search.query);
  } else {
    params.set("query", "");
  }
  if (search.showEmpty !== null && search.showEmpty !== undefined) {
    params.set("showEmpty", search.showEmpty);
  } else {
    params.set("showEmpty", true);
  }
  return params;
}

export function getDefaultNavSearchParams() {
  return { query: "", sort: "0", type: "0" };
}

export function getDefaultTagSearchParams() {
  return { query: "", showEmpty: true, showOnlyFollowing: false };
}

export function getDefaultLanguageSearchParams() {
  return { query: "", showEmpty: true };
}

export function fillDefaultNavSearchFromUrl(search) {
  if (!(search.query !== null && search.query !== undefined)) {
    search.query = "";
  }
  if (
    !(search.type !== null && search.type !== undefined && search.type !== "0")
  ) {
    search.type = "all";
  }
  if (
    !(search.sort !== null && search.sort !== undefined && search.sort !== "0")
  ) {
    search.sort = "no";
  }
  return search;
}

export function fillDefaultTagSearchFromUrl(search) {
  if (!(search.query !== null && search.query !== undefined)) {
    search.query = "";
  }
  if (!(search.showEmpty !== null && search.showEmpty !== undefined)) {
    search.showEmpty = true;
  }
  if (
    !(
      search.showOnlyFollowing !== null &&
      search.showOnlyFollowing !== undefined
    )
  ) {
    search.showOnlyFollowing = false;
  }
  return search;
}

export function fillDefaultLanguageSearchFromUrl(search) {
  if (!(search.query !== null && search.query !== undefined)) {
    search.query = "";
  }
  if (!(search.showEmpty !== null && search.showEmpty !== undefined)) {
    search.showEmpty = true;
  }
  return search;
}
