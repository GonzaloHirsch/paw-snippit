import React from "react";
import i18n from "./i18n";

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

// We also take into account i18n in the naming of the routes
const routes = [
  {
    path: "/",
    exact: true,
    name: i18n.t("nav.home"),
    component: Home,
  },
  { path: "/login", exact: true, name: i18n.t("nav.login"), component: Login },
  {
    path: "/signup",
    exact: true,
    name: i18n.t("nav.signup"),
    component: SignUp,
  },
  {
    path: "/snippets",
    exact: true,
    name: i18n.t("nav.snippets"),
    component: SnippetFeed,
  },
  {
    path: "/snippets/:id",
    exact: true,
    name: i18n.t("nav.snippetsDetail"),
    component: SnippetOverview,
  },
];

export default routes;
