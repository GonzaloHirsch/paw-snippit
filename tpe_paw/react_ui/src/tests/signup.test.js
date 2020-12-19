// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Signup from "../components/login/signup";
import { renderWithrouter } from "./utils/components";

const OK_USERNAME = "okusername";
const ERROR_400_USERNAME = "error401username";
const API_ERROR = "Invalid username from API";

// Mock server response
const server = setupServer(
  rest.post("http://localhost:8080/api/v1/users", (req, res, ctx) => {
    const info = req.body;
    if (info.username === OK_USERNAME) {
      return res(ctx.json({}));
    } else {
      return res(
        ctx.status(400),
        ctx.json({ errors: [{ username: API_ERROR }] })
      );
    }
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

beforeEach(() => renderWithrouter(<Signup />));

test("information set ok", async () => {
  const info = {
    username: "username",
    email: "ok@gmail.com",
    password: "password",
    repeatPassword: "password",
  };

  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: info.username },
  });
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: info.password },
  });
  fireEvent.change(screen.getByPlaceholderText("Email"), {
    target: { value: info.email },
  });
  fireEvent.change(screen.getByPlaceholderText("Repeat Password"), {
    target: { value: info.repeatPassword },
  });

  expect(screen.getByPlaceholderText("Username").value).toBe(info.username);
  expect(screen.getByPlaceholderText("Password").value).toBe(info.password);
  expect(screen.getByPlaceholderText("Email").value).toBe(info.email);
  expect(screen.getByPlaceholderText("Repeat Password").value).toBe(
    info.repeatPassword
  );
});

test("shows required errors", async () => {
  fireEvent.click(screen.getByText("Sign Up!"));

  expect(screen.getByText("User is required")).toBeInTheDocument();
  expect(screen.getByText("Email is required")).toBeInTheDocument();
  expect(screen.getAllByText("Password is required").length).toBe(2);
});

test("not shows password missmatch with missing fields", async () => {
  const info = {
    password: "password1",
    repeatPassword: "password2",
  };
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: info.password },
  });
  fireEvent.change(screen.getByPlaceholderText("Repeat Password"), {
    target: { value: info.repeatPassword },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(screen.queryByText("Passwords don't match")).not.toBeInTheDocument();
});

test("shows password missmatch", async () => {
  const info = {
    username: "username",
    email: "ok@gmail.com",
    password: "password1",
    repeatPassword: "password2",
  };

  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: info.username },
  });
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: info.password },
  });
  fireEvent.change(screen.getByPlaceholderText("Email"), {
    target: { value: info.email },
  });
  fireEvent.change(screen.getByPlaceholderText("Repeat Password"), {
    target: { value: info.repeatPassword },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(screen.getByText("Passwords don't match")).toBeInTheDocument();
});

test("shows username invalid", async () => {
  const info = {
    username: "#¢∞#¢|gdfdf",
  };
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: info.username },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(screen.getByText("Invalid username")).toBeInTheDocument();
});

test("shows username too small", async () => {
  const info = {
    username: "abc",
  };
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: info.username },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(
    screen.getByText("Username must be at least 6 characters long")
  ).toBeInTheDocument();
});

test("shows username too big", async () => {
  const info = {
    username: "abcde12345abcde12345abcde12345abcde12345abcde123451",
  };
  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: info.username },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(
    screen.getByText("Username must be at less than 50 characters long")
  ).toBeInTheDocument();
});

test("shows email invalid", async () => {
  const info = {
    email: "agmail.com",
  };
  fireEvent.change(screen.getByPlaceholderText("Email"), {
    target: { value: info.email },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(screen.getByText("Invalid email")).toBeInTheDocument();
});

test("shows password and repeatPassword too short", async () => {
  const info = {
    password: "pas",
    repeatPassword: "pas2",
  };
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: info.password },
  });
  fireEvent.change(screen.getByPlaceholderText("Repeat Password"), {
    target: { value: info.repeatPassword },
  });
  expect(
    screen.getAllByText("Password must be at least 8 characters long").length
  ).toBe(2);
});

test("shows password and repeatPassword invalid format", async () => {
  const info = {
    password: "pass word",
    repeatPassword: "pass word",
  };
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: info.password },
  });
  fireEvent.change(screen.getByPlaceholderText("Repeat Password"), {
    target: { value: info.repeatPassword },
  });
  expect(screen.getAllByText("Password can not contain spaces").length).toBe(2);
});

test("shows API errors", async () => {
  const info = {
    username: ERROR_400_USERNAME,
    email: "ok@gmail.com",
    password: "password",
    repeatPassword: "password",
  };

  fireEvent.change(screen.getByPlaceholderText("Username"), {
    target: { value: info.username },
  });
  fireEvent.change(screen.getByPlaceholderText("Password"), {
    target: { value: info.password },
  });
  fireEvent.change(screen.getByPlaceholderText("Email"), {
    target: { value: info.email },
  });
  fireEvent.change(screen.getByPlaceholderText("Repeat Password"), {
    target: { value: info.repeatPassword },
  });
  fireEvent.click(screen.getByText("Sign Up!"));
  expect(await screen.findByText(API_ERROR)).toBeInTheDocument();
});
