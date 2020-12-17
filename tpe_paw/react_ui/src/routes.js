import React from "react";
import i18n from "./i18n";
import { ROLE_USER, ROLE_ADMIN } from "./js/security_utils";

// Lazy loading the components https://reactjs.org/docs/code-splitting.html and https://github.com/ReactTraining/react-router/tree/master/packages/react-router-config
const SnippetOverview = React.lazy(() =>
  import("./components/pages/snippet_overview")
);
const SnippetCreate = React.lazy(() =>
  import("./components/snippets/snippet_create")
);
const ItemCreate = React.lazy(() => import("./components/items/item_create"));
const TagSnippets = React.lazy(() => import("./components/pages/tag_snippets"));
const LanguageSnippets = React.lazy(() =>
  import("./components/pages/language_snippets")
);
const Login = React.lazy(() => import("./components/login/login"));
const SignUp = React.lazy(() => import("./components/login/signup"));
const Home = React.lazy(() => import("./components/pages/home"));
const Flagged = React.lazy(() => import("./components/pages/flagged"));
const Favorites = React.lazy(() => import("./components/pages/favorites"));
const Following = React.lazy(() => import("./components/pages/following"));
const Upvoted = React.lazy(() => import("./components/pages/upvoted"));
const Explore = React.lazy(() => import("./components/pages/explore"));
const Languages = React.lazy(() => import("./components/pages/languages"));
const Tags = React.lazy(() => import("./components/pages/tags"));
const UserProfile = React.lazy(() => import("./components/pages/user_profile"));
const RecoverSend = React.lazy(() => import("./components/login/recover_send"));
const Goodbye = React.lazy(() => import("./components/pages/goodbye"));
const Page404 = React.lazy(() => import("./components/pages/errors/page_404"));
const Page500 = React.lazy(() => import("./components/pages/errors/page_500"));
const ChangePassword = React.lazy(() =>
  import("./components/login/change_password")
);
const Verify = React.lazy(() => import("./components/profile/verify"));

// We also take into account i18n in the naming of the routes
/*
 Each route must have a "roles" attribute, if the route is available for anyone, 
 the "roles" attribute must be empty([]). In case a user does not have the role for the page,
 the app redirects the user either to the login or to the home feed.
 Each route must have a "anon" attribute, it indicates that the route can only be navigated if no
 user is logged.
 Each route must have a "strict" attribute, it indicates that the user accessing must contain 
 exactly the listed roles
*/
const routes = [
  {
    path: "/",
    exact: true,
    name: i18n.t("nav.home"),
    component: Home,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/search",
    exact: true,
    name: i18n.t("nav.homeSearch"),
    component: Home,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/login",
    exact: true,
    name: i18n.t("nav.login"),
    component: Login,
    roles: [],
    anon: true,
    strict: false,
  },
  {
    path: "/signup",
    exact: true,
    name: i18n.t("nav.signup"),
    component: SignUp,
    roles: [],
    anon: true,
    strict: false,
  },
  {
    path: "/snippets/create",
    exact: true,
    name: i18n.t("snippetCreate.name"),
    component: SnippetCreate,
    roles: [ROLE_USER],
    anon: false,
    strict: true,
  },
  {
    path: "/items/create",
    exact: true,
    name: i18n.t("snippetCreate.name"), // TODO
    component: ItemCreate,
    roles: [ROLE_ADMIN],
    anon: false,
    strict: false, // Here is false because admins can be USER and ADMIN
  },
  {
    path: "/snippets/:id",
    exact: true,
    name: i18n.t("nav.snippetsDetail"),
    component: SnippetOverview,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/favorites",
    exact: true,
    name: i18n.t("nav.favorites"),
    component: Favorites,
    roles: [ROLE_USER, ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/favorites/search",
    exact: true,
    name: i18n.t("nav.favorites"),
    component: Favorites,
    roles: [ROLE_USER, ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/following",
    exact: false,
    name: i18n.t("nav.following"),
    component: Following,
    roles: [ROLE_USER, ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/flagged",
    exact: true,
    name: i18n.t("nav.flagged"),
    component: Flagged,
    roles: [ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/flagged/search",
    exact: true,
    name: i18n.t("nav.flagged"),
    component: Flagged,
    roles: [ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/goodbye",
    exact: true,
    name: i18n.t("nav.goodbye"),
    component: Goodbye,
    roles: [],
    anon: true,
    strict: false,
  },
  {
    path: "/upvoted",
    exact: true,
    name: i18n.t("nav.upvoted"),
    component: Upvoted,
    roles: [ROLE_USER, ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/upvoted/search",
    exact: true,
    name: i18n.t("nav.upvoted"),
    component: Upvoted,
    roles: [ROLE_USER, ROLE_ADMIN],
    anon: false,
    strict: false,
  },
  {
    path: "/explore",
    exact: true,
    name: i18n.t("nav.explore"),
    component: Explore,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/explore/search",
    exact: true,
    name: i18n.t("nav.explore"),
    component: Explore,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/recover",
    exact: true,
    name: i18n.t("nav.recover"),
    component: RecoverSend,
    roles: [],
    anon: true,
    strict: false,
  },
  {
    path: "/reset-password",
    exact: false,
    name: i18n.t("nav.resetPassword"),
    component: ChangePassword,
    roles: [],
    anon: true,
    strict: false,
  },
  {
    path: "/verify",
    exact: false,
    name: i18n.t("nav.verify"),
    component: Verify,
    roles: [ROLE_USER],
    anon: false,
    strict: false,
  },
  {
    path: "/user/:id",
    exact: true,
    name: i18n.t("nav.users"), // TODO FIX ME
    component: UserProfile,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/user/:id/search",
    exact: true,
    name: i18n.t("nav.users"), // TODO FIX ME
    component: UserProfile,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/profile",
    exact: true,
    name: i18n.t("nav.profile"),
    component: UserProfile,
    roles: [ROLE_USER],
    anon: false,
    strict: true,
  },
  {
    path: "/profile/search",
    exact: true,
    name: i18n.t("nav.profile"),
    component: UserProfile,
    roles: [ROLE_USER],
    anon: false,
    strict: true,
  },
  {
    path: "/languages",
    exact: true,
    name: i18n.t("nav.languages"),
    component: Languages,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/languages/search",
    exact: true,
    name: i18n.t("nav.languages"),
    component: Languages,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/languages/:id",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Language" }),
    component: LanguageSnippets,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/languages/:id/search",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Language" }),
    component: LanguageSnippets,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/tags",
    exact: true,
    name: i18n.t("nav.tags"),
    component: Tags,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/tags/search",
    exact: true,
    name: i18n.t("nav.tags"),
    component: Tags,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/tags/:id",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Tag" }),
    component: TagSnippets,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/tags/:id/search",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Tag" }),
    component: TagSnippets,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/404",
    exact: true,
    name: i18n.t("nav.error_404"),
    component: Page404,
    roles: [],
    anon: false,
    strict: false,
  },
  {
    path: "/500",
    exact: true,
    name: i18n.t("nav.error_500"),
    component: Page500,
    roles: [],
    anon: false,
    strict: false,
  },
];

export default routes;
