import { render } from "@testing-library/react";
import { Router as Router } from "react-router-dom";
import { createMemoryHistory } from "history";

// Renders a component wrapped in a Router component, creates a history, location and state for it have it as normally
export function renderWithrouter(comp) {
  const history = createMemoryHistory();
  const state = { a: 123, b: 456 };
  history.push("/", state);
  render(<Router history={history}>{comp}</Router>);
}
