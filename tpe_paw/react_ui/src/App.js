import React from "react";
import { HashRouter as Router, Switch, Route } from "react-router-dom";
import logo from "./logo.svg";

// Styles
import "./App.scss";

// Components
import NavBar from "./components/navigation/navbar";
import SnippetCard from "./components/snippets/snippet_card";
import Login from "./components/login/login";
import SnippetDetail from "./components/snippets/snippet_detail";

function App() {
  return (
    // All components inside the Router tags can use the routing
    // The Switch is used in order to avoid multiple component rendering
    // The "exact" keyword makes sure that the path is exactly matched
    <React.Fragment>
      <Router>
        <NavBar/>
        <Switch>
          <Route path="/" exact />
          <Route path="/login" component={Login} />
          <Route path="/snippet" exact component={SnippetDetail} />
          <Route path="/snippet/:id" component={SnippetDetail} />
        </Switch>
        <SnippetCard/>
      </Router>
    </React.Fragment>
  );
}

export default App;
