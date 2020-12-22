export const CONTEXT = {
  HOME: "home",
  FAVORITES: "favorites",
  EXPLORE: "explore",
  TAGS: "tags",
  TAGS_SNIPPETS: "tags_snippets",
  LANGUAGES: "languages",
  LANGUAGES_SNIPPETS: "languages_snippets",
  PROFILE: "profile",
  FLAGGED: "flagged",
  FOLLOWING: "following",
  UPVOTED: "upvoted",
  ERROR: "error",
  LOGIN: "login",
  SIGNUP: "signup",
  RECOVER: "recover",
  VERIFY: "verify",
  RESET_PASSWORD: "reset-password",
  GOODBYE: "goodbye",
  SNIPPET_CREATE: "snippet-create",
  ITEMS_CREATE: "items-create",
  SNIPPETS: "snippets"
};

export const EXPLORE = {
  FIELD: "field",
  SORT: "sort",
  FLAGGED: "includeFlagged",
  TITLE: "title",
  LANGUAGE: "language",
  TAG: "tag",
  USERNAME: "username",
  MINREP: "minRep",
  MAXREP: "maxRep",
  MINDATE: "minDate",
  MAXDATE: "maxDate",
  MINVOTES: "minVotes",
  MAXVOTES: "maxVotes",
};

export const ITEM_TYPES = {
  LANGUAGE: "language",
  TAG: "tag",
};

export const EXPLORE_ORDERBY = ["date", "reputation", "votes", "title"];
export const SORT = ["asc", "desc", "no"];
export const MIN_INTEGER = -2147483647;
export const MAX_INTEGER = 2147483647;
export const ACTIVE_USER_SNIPPETS = "active";
export const DELETED_USER_SNIPPETS = "deleted";
export const FOLLOWING_LIST_LIMIT = 25;
export const CONTEXT_WITHOUT_SEARCH = [
  CONTEXT.ERROR,
  CONTEXT.EXPLORE,
  CONTEXT.LOGIN,
  CONTEXT.SIGNUP,
  CONTEXT.RECOVER,
  CONTEXT.VERIFY,
  CONTEXT.RESET_PASSWORD,
  CONTEXT.GOODBYE,
  CONTEXT.SNIPPET_CREATE,
  CONTEXT.ITEMS_CREATE,
  CONTEXT.SNIPPETS
];
