import React, { Component, Suspense } from "react";
import {
  HashRouter as Router,
  Switch,
  Route,
  withRouter,
  Redirect
} from "react-router-dom";
import routes from "../routes";
import { PrivateRoute } from "./navigation/private_route";
import NavBar from "./navigation/navbar";


class AppComponent extends Component {
  state = {};

  // Function to determine the route component based on if the route is role protected or not
  getRouteComponent(route) {
    // We check if the roles for the route are empty, in that case it is NOT role protected
    // It might be the case that a route roles are empty, but anonymous usage is required
    if (route.roles.length > 0 || route.anon) {
      return (
        <PrivateRoute
          key={route.name}
          path={route.path}
          exact={route.exact}
          roles={route.roles}
          comp={route.component}
          anon={route.anon}
          strict={route.strict}
        ></PrivateRoute>
      );
    } else {
      return (
        <Route
          key={route.name}
          path={route.path}
          exact={route.exact}
          component={route.component}
        />
      );
    }
  }

  render() {
    return (
      <Suspense
        fallback={
          <div className="pt-1 align-items-horizontal-center align-items-vertical mt-5">
           Snippit is Loading...
          </div>
        }
      >
        <Router>
          <NavBar className="nav-spacing" />
          <Switch>
            {routes.map((route) => this.getRouteComponent(route))}
            <Redirect to="/404"/>
          </Switch>
        </Router>
      </Suspense>
    );
  }
}

export default AppComponent;
