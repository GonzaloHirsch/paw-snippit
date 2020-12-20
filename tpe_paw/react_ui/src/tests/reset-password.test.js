// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import ResetPassword from "../components/login/change_password";
import { renderWithrouter } from "./utils/components";

const OK_TOKEN = "oktoken";
const ERROR_TOKEN = "errortoken";

// Mock server response
const server = setupServer(
  rest.get(
    "http://localhost:8080/api/v1/users/:id/valid_token",
    (req, res, ctx) => {
      const urltoken = req.url.searchParams.get("token");
      if (urltoken === OK_TOKEN) {
        return res(ctx.status(204), ctx.json({}));
      } else {
        return res(ctx.status(400), ctx.json({ errors: [{}] }));
      }
    }
  )
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test("invalid token generates expired message", async () => {
  renderWithrouter(
    <ResetPassword />,
    "/reset-password?id=1&token=" + ERROR_TOKEN
  );

  expect(await screen.findByText("The link has expired")).toBeInTheDocument();
});

test("valid token makes passwords available", async () => {
  renderWithrouter(<ResetPassword />, "/reset-password?id=1&token=" + OK_TOKEN);

  expect(await screen.findByPlaceholderText("Password")).toBeInTheDocument();
  expect(
    await screen.findByPlaceholderText("Repeat password")
  ).toBeInTheDocument();
});
