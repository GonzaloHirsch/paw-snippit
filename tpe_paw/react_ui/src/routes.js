import React from "react";
import i18n from "./i18n";
import { ROLE_USER, ROLE_ADMIN } from "./js/security_utils";

// Lazy loading the components https://reactjs.org/docs/code-splitting.html and https://github.com/ReactTraining/react-router/tree/master/packages/react-router-config
const SnippetOverview = React.lazy(() =>
  import("./components/pages/snippet_overview")
);
const SnippetFeed = React.lazy(() =>
  import("./components/snippets/snippet_feed")
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
 the app redirects the user either to the login or to the home feed
*/
const routes = [
  {
    path: "/",
    exact: true,
    name: i18n.t("nav.home"),
    component: Home,
    roles: [],
  },
  {
    path: "/search",
    exact: true,
    name: i18n.t("nav.homeSearch"),
    component: Home,
    roles: [],
  },
  {
    path: "/login",
    exact: true,
    name: i18n.t("nav.login"),
    component: Login,
    roles: [],
  },
  {
    path: "/signup",
    exact: true,
    name: i18n.t("nav.signup"),
    component: SignUp,
    roles: [],
  },
  {
    path: "/snippets/create",
    exact: true,
    name: i18n.t("snippetCreate.name"),
    component: SnippetCreate,
    roles: [ROLE_USER],
  },
  {
    path: "/items/create",
    exact: true,
    name: i18n.t("snippetCreate.name"), // TODO
    component: ItemCreate,
    roles: [ROLE_ADMIN],
  },
  {
    path: "/snippets/:id",
    exact: true,
    name: i18n.t("nav.snippetsDetail"),
    component: SnippetOverview,
    roles: [],
  },
  {
    path: "/favorites",
    exact: true,
    name: i18n.t("nav.favorites"),
    component: Favorites,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/favorites/search",
    exact: true,
    name: i18n.t("nav.favorites"),
    component: Favorites,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/flagged",
    exact: true,
    name: i18n.t("nav.flagged"),
    component: Flagged,
    roles: [ROLE_ADMIN],
  },
  {
    path: "/flagged/search",
    exact: true,
    name: i18n.t("nav.flagged"),
    component: Flagged,
    roles: [ROLE_ADMIN],
  },
  {
    path: "/goodbye",
    exact: true,
    name: i18n.t("nav.goodbye"),
    component: Goodbye,
    roles: [],
  },
  {
    path: "/upvoted",
    exact: true,
    name: i18n.t("nav.upvoted"),
    component: Upvoted,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/upvoted/search",
    exact: true,
    name: i18n.t("nav.upvoted"),
    component: Upvoted,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/explore",
    exact: true,
    name: i18n.t("nav.explore"),
    component: Explore,
    roles: [],
  },
  {
    path: "/explore/search",
    exact: true,
    name: i18n.t("nav.explore"),
    component: Explore,
    roles: [],
  },
  {
    path: "/recover",
    exact: true,
    name: i18n.t("nav.recover"),
    component: RecoverSend,
    roles: [],
  },
  {
    path: "/reset-password",
    exact: false,
    name: i18n.t("nav.resetPassword"),
    component: ChangePassword,
    roles: [],
  },
  {
    path: "/verify",
    exact: false,
    name: i18n.t("nav.verify"),
    component: Verify,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/user/:id",
    exact: true,
    name: i18n.t("nav.users"), // TODO FIX ME
    component: UserProfile,
    roles: [],
  },
  {
    path: "/user/:id/search",
    exact: true,
    name: i18n.t("nav.users"), // TODO FIX ME
    component: UserProfile,
    roles: [],
  },
  {
    path: "/profile",
    exact: true,
    name: i18n.t("nav.profile"),
    component: UserProfile,
    roles: [ROLE_USER],
  },
  {
    path: "/profile/search",
    exact: true,
    name: i18n.t("nav.profile"),
    component: UserProfile,
    roles: [ROLE_USER],
  },
  {
    path: "/languages",
    exact: true,
    name: i18n.t("nav.languages"),
    component: Languages,
    roles: [],
  },
  {
    path: "/languages/search",
    exact: true,
    name: i18n.t("nav.languages"),
    component: Languages,
    roles: [],
  },
  {
    path: "/languages/:id",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Language" }),
    component: LanguageSnippets,
    roles: [],
  },
  {
    path: "/languages/:id/search",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Language" }),
    component: LanguageSnippets,
    roles: [],
  },
  {
    path: "/tags",
    exact: true,
    name: i18n.t("nav.tags"),
    component: Tags,
    roles: [],
  },
  {
    path: "/tags/search",
    exact: true,
    name: i18n.t("nav.tags"),
    component: Tags,
    roles: [],
  },
  {
    path: "/tags/:id",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Tag" }),
    component: TagSnippets,
    roles: [],
  },
  {
    path: "/tags/:id/search",
    exact: true,
    name: i18n.t("items.snippetsFor", { item: "Tag" }),
    component: TagSnippets,
    roles: [],
  },
  {
    path: "/404",
    exact: true,
    name: i18n.t("nav.error_404"),
    component: Page404,
    roles: [],
  },
  {
    path: "/500",
    exact: true,
    name: i18n.t("nav.error_500"),
    component: Page500,
    roles: [],
  },
];

export default routes;
