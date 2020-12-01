import React, { Component, Suspense } from "react";
import { HashRouter as Router, Switch, Route } from "react-router-dom";
import routes from "../routes";
import { PrivateRoute } from "./navigation/private_route";
import NavBar from "./navigation/navbar";

class AppComponent extends Component {
  state = {};

  constructor(props) {
    super(props);
    this.handleSearch = this.handleSearch.bind(this);
  }

  // Function to determine the route component based on if the route is role protected or not
  getRouteComponent(route) {
    // We check if the roles for the route are empty, in that case it is NOT role protected
    if (route.roles.length > 0) {
      return (
        <PrivateRoute
          key={route.name}
          path={route.path}
          exact={route.exact}
          roles={route.roles}
          comp={route.component}
        ></PrivateRoute>
      );
    } else {
      return (
        <Route key={route.name} path={route.path} exact={route.exact}>
          <route.component
            query={this.state.query}
            filter={this.state.filter}
            order={this.state.order}
          />
        </Route>
      );
    }
  }

  handleSearch(query, filter, order) {
    this.setState({ query: query, filter: filter, order: order });
  }

  render() {
    return (
      <Suspense fallback={<div>Loading...</div>}>
        <Router>
          <NavBar className="nav-spacing" onSearch={this.handleSearch} />
          <Switch>
            {routes.map((route) => this.getRouteComponent(route))}
          </Switch>
        </Router>
      </Suspense>
    );
  }
}

export default AppComponent;
