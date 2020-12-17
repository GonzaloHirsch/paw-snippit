import React, { Component, Suspense } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import routes from "./routes";
import logo from "./logo.svg";
import { PrivateRoute } from "./components/navigation/private_route";
import AppComponent from "./components/app_component";

// Styles
import "./App.scss";

function App() {
  return (
    // All components inside the Router tags can use the routing
    // The Switch is used in order to avoid multiple component rendering
    // The "exact" keyword makes sure that the path is exactly matched
    <React.Fragment>
      <AppComponent />
    </React.Fragment>
  );
}

export default App;
