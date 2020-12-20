// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import RecoverSend from "../components/login/recover_send";
import { renderWithrouter } from "./utils/components";

const OK_EMAIL = "ok@gmail.com";
const ERROR_EMAIL = "error@gmail.com";

// Mock server response
const server = setupServer(
  rest.post(
    "http://localhost:8080/api/v1/users/recover_password",
    (req, res, ctx) => {
      const email = req.body.email;
      if (email === OK_EMAIL) {
        return res(ctx.status(204), ctx.json({}));
      } else {
        return res(
          ctx.status(400),
          ctx.json({ errors: [{ email: "Invalid email" }] })
        );
      }
    }
  )
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
beforeEach(() => renderWithrouter(<RecoverSend />));

test("show empty email validation", async () => {
  fireEvent.click(screen.getByText("Send Recovery Email"));
  expect(screen.getByText("Email is required")).toBeInTheDocument();
});

test("show invalid email format validation", async () => {
  const email = "agmail.com";
  fireEvent.change(screen.getByPlaceholderText("Insert your email here"), {
    target: { value: email },
  });
  expect(screen.getByText("Invalid email")).toBeInTheDocument();
});

test("show invalid email from API", async () => {
  fireEvent.change(screen.getByPlaceholderText("Insert your email here"), {
    target: { value: ERROR_EMAIL },
  });
  fireEvent.click(screen.getByText("Send Recovery Email"));
  expect(await screen.findByText("Invalid email")).toBeInTheDocument();
});

test("show email sent", async () => {
  fireEvent.change(screen.getByPlaceholderText("Insert your email here"), {
    target: { value: OK_EMAIL },
  });
  fireEvent.click(screen.getByText("Send Recovery Email"));
  expect(
    await screen.findByText(
      "A link has been sent to your email to complete the password recovery process"
    )
  ).toBeInTheDocument();
});
