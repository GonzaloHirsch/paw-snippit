// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Tags from "../components/pages/tags";
import { renderWithrouter } from "./utils/components";
import store from "../store";
import { loginSuccess } from "../redux/actions/actionCreators";

const API_DATA = [
  { empty: true, following: false, id: 246, name: "class" },
  { empty: true, following: false, id: 255, name: "coding-style" },
  { empty: true, following: false, id: 316, name: "comparison" },
  { empty: true, following: false, id: 199, name: "computer-vision" },
  { empty: true, following: true, id: 296, name: "concatenation" },
  { empty: true, following: false, id: 317, name: "conditional" },
  { empty: true, following: true, id: 186, name: "configuration" },
  { empty: true, following: false, id: 287, name: "constraints" },
  { empty: true, following: true, id: 256, name: "cookies" },
  { empty: true, following: false, id: 136, name: "cordova" },
  { empty: true, following: true, id: 185, name: "count" },
  { empty: true, following: true, id: 188, name: "cron" },
  { empty: true, following: false, id: 224, name: "cryptography" },
  { empty: true, following: false, id: 302, name: "css-animations" },
  { empty: true, following: true, id: 196, name: "css-selectors" },
  { empty: true, following: false, id: 152, name: "curl" },
  { empty: true, following: true, id: 271, name: "cypher" },
  { empty: true, following: true, id: 257, name: "data-structures" },
  { empty: false, following: false, id: 117, name: "database" },
  { empty: true, following: true, id: 300, name: "database-connection" },
];

// Mock server response
const server = setupServer(
  rest.get("http://localhost:8080/api/v1/tags", (req, res, ctx) => {
    return res(ctx.json(API_DATA));
  }),
  rest.put("http://localhost:8080/api/v1/tags/:id/follow", (req, res, ctx) => {
    return res(ctx.json({}));
  }),
  rest.delete(
    "http://localhost:8080/api/v1/tags/:id/follow",
    (req, res, ctx) => {
      return res(ctx.json({}));
    }
  )
);

beforeAll(() => {
  server.listen();
  const token =
    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHb256YWxvSGlyc2NoIiwidWlkIjo0NiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiaXNzIjoic25pcHBpdC5wYXcuaXRiYSIsImlhdCI6MTYwNzQ0MTc2NiwiZXhwIjoxNjA3NTI4MTY2fQ.QwSYsb0-WJyLLVCZ9zi2He9aKWh_KtoMfzp-yZAHULLWjLReTwrpdc6qjE8AjdeBfXRkgskUwRFfDIEHgaGCoA";
  const refreshToken =
    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHb256YWxvSGlyc2NoIiwidWlkIjo0NiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiaXNzIjoic25pcHBpdC5wYXcuaXRiYSIsImlhdCI6MTYwNzQ0MTc2NiwiZXhwIjoxNjA3NTI4MTY2fQ.QwSYsb0-WJyLLVCZ9zi2He9aKWh_KtoMfzp-yZAHULLWjLReTwrpdc6qjE8AjdeBfXRkgskUwRFfDIEHgaGCoA";
  store.dispatch(loginSuccess({ token }, { refreshToken }, true));
});
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

beforeEach(() => renderWithrouter(<Tags />));

test("renders tags ok", async () => {
  API_DATA.forEach(async (i) => {
    expect(await screen.findByText(i.name.toUpperCase())).toBeInTheDocument();
  });
});

// Throws many warnings regarding some act() function but don't know how to solve
/* test("follow a tag and see amount of tags to follow decrease", async () => {
  const prevFollowItems = await screen.findAllByText("Follow");
  const prevFollowCount = prevFollowItems.length;
  const followItem = prevFollowItems[0];
  fireEvent.click(followItem);
  const newFollowItems = await screen.findAllByText("Follow");
  const newFollowCount = newFollowItems.length;
  expect(newFollowCount).toBe(prevFollowCount - 1);
});

test("unfollow a tag and see amount of tags to unfollow decrease", async () => {
  const prevFollowItems = await screen.findAllByText("Unfollow");
  const prevFollowCount = prevFollowItems.length;
  const followItem = prevFollowItems[0];
  fireEvent.click(followItem);
  const newFollowItems = await screen.findAllByText("Unfollow");
  const newFollowCount = newFollowItems.length;
  expect(newFollowCount).toBe(prevFollowCount - 1);
}); */
