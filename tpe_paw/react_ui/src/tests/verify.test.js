// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Verify from "../components/profile/verify";
import { renderWithrouter } from "./utils/components";
import store from "../store";
import { loginSuccess } from "../redux/actions/actionCreators";

const OK_CODE = "123456";
const ERROR_CODE = "654321";

// Mock server response
const server = setupServer(
  rest.post(
    "http://localhost:8080/api/v1/users/:id/verify_email",
    (req, res, ctx) => {
      const code = req.body.code;
      if (code === OK_CODE) {
        return res(ctx.status(204), ctx.json({}));
      } else {
        return res(ctx.status(400), ctx.json({ errors: [{}] }));
      }
    }
  ),
  rest.get("http://localhost:8080/api/v1/users/:id", (req, res, ctx) => {
    return res(
      ctx.json({
        activeSnippets: "http://localhost:8080/api/v1/users/46/active_snippets",
        dateJoined: "2020-04-27T18:08:36.971Z",
        deletedSnippets:
          "http://localhost:8080/api/v1/users/46/deleted_snippets",
        description:
          "This is my fantastic description, but it is way too long but hey, it has runes ᛞᚫᛚᚪᚾ yyaaaaayy cheche\nᛞᚫᛚᚪᚾ yyaaaaayy",
        hasPicture: true,
        id: 46,
        picture: "http://localhost:8080/api/v1/users/46/profile_photo",
        stats: { activeSnippetCount: 13, followingCount: 26, reputation: -43 },
        url: "http://localhost:8080/api/v1/users/46",
        username: "GonzaloHirsch",
        verified: false,
      })
    );
  })
);

beforeAll(() => {
  server.listen();
  // Dispatch the login event
  const token =
    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHb256YWxvSGlyc2NoIiwidWlkIjo0NiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiaXNzIjoic25pcHBpdC5wYXcuaXRiYSIsImlhdCI6MTYwNzQ0MTc2NiwiZXhwIjoxNjA3NTI4MTY2fQ.QwSYsb0-WJyLLVCZ9zi2He9aKWh_KtoMfzp-yZAHULLWjLReTwrpdc6qjE8AjdeBfXRkgskUwRFfDIEHgaGCoA";
  const refreshToken =
    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHb256YWxvSGlyc2NoIiwidWlkIjo0NiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiaXNzIjoic25pcHBpdC5wYXcuaXRiYSIsImlhdCI6MTYwNzQ0MTc2NiwiZXhwIjoxNjA3NTI4MTY2fQ.QwSYsb0-WJyLLVCZ9zi2He9aKWh_KtoMfzp-yZAHULLWjLReTwrpdc6qjE8AjdeBfXRkgskUwRFfDIEHgaGCoA";
  store.dispatch(loginSuccess({ token }, { refreshToken }, true));
});
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
beforeEach(() => renderWithrouter(<Verify />));

test("show invalid code length", async () => {
  const code = "123";
  fireEvent.change(screen.getByPlaceholderText("Code"), {
    target: { value: code },
  });
  expect(screen.getByText("Code must be 6 digits long")).toBeInTheDocument();
});

test("show invalid code format", async () => {
  const code = "adfgbs";
  fireEvent.change(screen.getByPlaceholderText("Code"), {
    target: { value: code },
  });
  expect(screen.getByText("Code must contain only digits")).toBeInTheDocument();
});

test("show empty code validation", async () => {
  fireEvent.click(screen.getByText("Verify"));
  expect(screen.getByText("Code is required")).toBeInTheDocument();
});

test("show invalid code response from api", async () => {
  fireEvent.change(screen.getByPlaceholderText("Code"), {
    target: { value: ERROR_CODE },
  });
  fireEvent.click(screen.getByText("Verify"));
  expect(await screen.findByText("Code is invalid")).toBeInTheDocument();
});
