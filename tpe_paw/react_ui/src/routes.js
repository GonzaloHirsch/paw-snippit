import React from "react";
import i18n from "/i18n";

// Lazy loading the components https://reactjs.org/docs/code-splitting.html and https://github.com/ReactTraining/react-router/tree/master/packages/react-router-config
const SnippetDetail = React.lazy(() =>
  import("./components/snippets/snippet_detail")
);
const Login = React.lazy(() => import("./components/login/login"));

// We also take into account i18n in the naming of the routes
const routes = [
  {
    path: "/",
    exact: true,
    name: i18n.t("nav.home"),
    component: Login,
    roles: [],
  },
  { path: "/login", name: i18n.t("nav.login"), component: Login, roles: [] },
  {
    path: "/snippets",
    exact: true,
    name: i18n.t("nav.snippets"),
    component: SnippetDetail,
    roles: [],
  },
  {
    path: "/snippets/:id",
    exact: true,
    name: i18n.t("nav.snippets_detail"),
    component: SnippetDetail,
    roles: [],
  },
];

export default routes;
