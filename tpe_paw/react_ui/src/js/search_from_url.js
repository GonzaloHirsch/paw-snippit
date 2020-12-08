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
