import React, { Component, Suspense } from "react";
import { HashRouter as Router, Switch, Route } from "react-router-dom";
import routes from "./routes";
import logo from "./logo.svg";
import { PrivateRoute } from "./components/navigation/private_route";

// Styles
import "./App.scss";

// Components
import NavBar from "./components/navigation/navbar";

function App() {
  return (
    // All components inside the Router tags can use the routing
    // The Switch is used in order to avoid multiple component rendering
    // The "exact" keyword makes sure that the path is exactly matched
    <React.Fragment>
      <Suspense fallback={<div>Loading...</div>}>
        <Router>
          <NavBar className="nav-spacing" />
          <Switch>{routes.map((route) => getRouteComponent(route))}</Switch>
        </Router>
      </Suspense>
    </React.Fragment>
  );
}

// Function to determine the route component based on if the route is role protected or not
function getRouteComponent(route) {
  // We check if the roles for the route are empty, in that case it is NOT role protected
  if (route.roles.length > 0) {
    return (
      <PrivateRoute
        key={route.name}
        path={route.path}
        exact={route.exact}
        roles={route.roles}
        component={route.component}
      ></PrivateRoute>
    );
  } else {
    return (
      <Route
        key={route.name}
        path={route.path}
        component={route.component}
        exact={route.exact}
      />
    );
  }
}

export default App;
