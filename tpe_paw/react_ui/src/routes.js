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
const Login = React.lazy(() => import("./components/login/login"));
const SignUp = React.lazy(() => import("./components/login/signup"));
const Home = React.lazy(() => import("./components/pages/home"));
const Favorites = React.lazy(() => import("./components/pages/favorites"));
const Upvoted = React.lazy(() => import("./components/pages/upvoted"));
const RecoverSend = React.lazy(() => import("./components/login/recover_send"));

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
    path: "/snippets",
    exact: true,
    name: i18n.t("nav.snippets"),
    component: SnippetFeed,
    roles: [],
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
    exact: false,
    name: i18n.t("nav.favorites"),
    component: Favorites,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/upvoted",
    exact: false,
    name: i18n.t("nav.upvoted"),
    component: Upvoted,
    roles: [ROLE_USER, ROLE_ADMIN],
  },
  {
    path: "/recover",
    exact: true,
    name: i18n.t("nav.recover"),
    component: RecoverSend,
    roles: [],
  },
];

export default routes;
