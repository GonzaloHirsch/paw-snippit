// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Login from "../components/login/login";
import { renderWithrouter } from "./utils/components";

const server = setupServer(
  rest.post("/api/v1/login", (req, res, ctx) => {
    return res(ctx.json({ greeting: "hello there" }));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test("username and password set ok", async () => {
  renderWithrouter(<Login />);

  const username = "myusername";
  const password = "mypassword";

  fireEvent.change(screen.getByPlaceholderText("Username"), { target: { value: username } })
  fireEvent.change(screen.getByPlaceholderText("Password"), { target: { value: password } })
  
  expect(screen.getByPlaceholderText("Username").value).toBe(username);
  expect(screen.getByPlaceholderText("Password").value).toBe(password);
});

test("shows required errors in login", async () => {
  renderWithrouter(<Login />);

  fireEvent.click(screen.getByText("Log In"));

  expect(screen.getByText("User is required")).toBeInTheDocument();
  expect(screen.getByText("Password is required")).toBeInTheDocument();
});
/* 
test('handles server error', async () => {
  server.use(
    rest.get('/greeting', (req, res, ctx) => {
      return res(ctx.status(500))
    })
  )

  render(<Fetch url="/greeting" />)

  fireEvent.click(screen.getByText('Load Greeting'))

  await waitFor(() => screen.getByRole('alert'))

  expect(screen.getByRole('alert')).toHaveTextContent('Oops, failed to fetch!')
  expect(screen.getByRole('button')).not.toHaveAttribute('disabled')
}) */
