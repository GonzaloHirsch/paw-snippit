// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Languages from "../components/pages/languages";
import { renderWithrouter } from "./utils/components";

const API_DATA = [
  { empty: true, id: 141, name: "535f32" },
  { empty: true, id: 126, name: "562d" },
  { empty: false, id: 153, name: "Jává" },
  { empty: true, id: 152, name: "Pythón" },
  { empty: true, id: 151, name: "_hola_" },
  { empty: true, id: 104, name: "aaaaa" },
  { empty: false, id: 118, name: "adgaas" },
  { empty: true, id: 121, name: "adsfs" },
  { empty: true, id: 138, name: "asasdfsdfs" },
  { empty: true, id: 128, name: "asdfaga" },
  { empty: true, id: 139, name: "asdfasdf" },
  { empty: true, id: 124, name: "asfd" },
  { empty: true, id: 105, name: "bbbbb" },
  { empty: true, id: 144, name: "bvscre" },
  { empty: false, id: 89, name: "c" },
];

// Mock server response
const server = setupServer(
  rest.get("http://localhost:8080/api/v1/languages", (req, res, ctx) => {
    return res(ctx.json(API_DATA));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

beforeEach(() => renderWithrouter(<Languages />));

test("renders languages ok", async () => {
  API_DATA.forEach(async (i) => {
    expect(await screen.findByText(i.name.toUpperCase())).toBeInTheDocument();
  });
});
