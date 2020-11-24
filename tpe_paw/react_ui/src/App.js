import React, {Suspense} from "react";
import { HashRouter as Router, Switch, Route } from "react-router-dom";
import routes from "./routes";
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
      <Suspense fallback={<div>Loading...</div>}>
        <Router>
          <NavBar />
          <Switch>
            {/* <Route path="/" exact />
          <Route path="/login" component={Login} />
          <Route path="/snippet" exact component={SnippetDetail} />
          <Route path="/snippet/:id" component={SnippetDetail} /> */}

            {routes.map((route) => (
              <Route
                key={route.name}
                path={route.path}
                component={route.component}
                exact={route.exact}
              />
            ))}
          </Switch>
          <SnippetCard />
        </Router>
      </Suspense>
    </React.Fragment>
  );
}

export default App;
