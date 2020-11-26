import React from "react";
import i18n from "./i18n";

// Lazy loading the components https://reactjs.org/docs/code-splitting.html and https://github.com/ReactTraining/react-router/tree/master/packages/react-router-config
const SnippetDetail = React.lazy(() =>
  import("./components/snippets/snippet_detail")
);
const SnippetFeed = React.lazy(() =>
  import("./components/snippets/snippet_feed")
);
const Login = React.lazy(() => import("./components/login/login"));
const Home = React.lazy(() => import("./components/pages/home"));
const Favorites = React.lazy(() => import("./components/pages/favorites"));

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
    path: "/snippets",
    exact: true,
    name: i18n.t("nav.snippets"),
    component: SnippetFeed,
  },
  {
    path: "/snippets/:id",
    exact: true,
    name: i18n.t("nav.snippets_detail"),
    component: SnippetDetail,
  },
];

export default routes;
