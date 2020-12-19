// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Login from "../components/login/login";
import { renderWithrouter } from "./utils/components";

const OK_USERNAME = "okusername";
const ERROR_500_USERNAME = "error500username";
const ERROR_401_USERNAME = "error401username";

// Mock server response
const server = setupServer(
  rest.post("http://localhost:8080/api/v1/auth/login", (req, res, ctx) => {
    if (req.body.username === OK_USERNAME) {
      return res(
        ctx.json({
          token: "Bearer 1234567890",
          refreshToken: "Bearer 1234567891",
        })
      );
    } else if (req.body.username === ERROR_401_USERNAME) {
      return res(ctx.status(401), ctx.json({}));
    } else {
      return res(ctx.status(500), ctx.json({}));
    }
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

beforeEach(() => renderWithrouter(<Login />));

test("username and password set ok", async () => {
  const username = "myusername";
  const password = "mypassword";

  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: username },
  });
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: password },
  });

  expect(screen.getByPlaceholderText("Username").value).toBe(username);
  expect(screen.getByPlaceholderText("Password").value).toBe(password);
});

test("shows required errors in login", async () => {
  fireEvent.click(screen.getByText("Log In"));

  expect(screen.getByText("User is required")).toBeInTheDocument();
  expect(screen.getByText("Password is required")).toBeInTheDocument();
});

test("shows username missing characters error", async () => {
  const username = "user";
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: username },
  });
  expect(
    screen.getByText("Username must be at least 6 characters long")
  ).toBeInTheDocument();
});

test("shows username too long error", async () => {
  const username = "user123456user123456user123456user123456user1234561";
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: username },
  });
  expect(
    screen.getByText("Username must be at less than 50 characters long")
  ).toBeInTheDocument();
});

test("shows username invalid format error", async () => {
  const username = "%%%,,!/sdfd";
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: username },
  });
  expect(screen.getByText("Invalid username")).toBeInTheDocument();
});

test("shows password missing characters error", async () => {
  const password = "pass";
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: password },
  });
  expect(
    screen.getByText("Password must be at least 8 characters long")
  ).toBeInTheDocument();
});

test("shows password invalid format error", async () => {
  const password = "hola hola";
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: password },
  });
  expect(
    screen.getByText("Password can not contain spaces")
  ).toBeInTheDocument();
});

test("shows invalid credentials error from api", async () => {
  const password = "password";
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: ERROR_401_USERNAME },
  });
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: password },
  });

  fireEvent.click(screen.getByText("Log In"));

  expect(await screen.findByText("Invalid username or password")).toBeInTheDocument();
});
