// __tests__/fetch.test.js
import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import SnippetOverview from "../components/pages/snippet_overview";

const server = setupServer(
  rest.get("http://localhost:8080/api/v1/snippets/:id", (req, res, ctx) => {
    return res(
      ctx.json({
        code: "My code is amazting",
        createdDate: "2020-12-17T13:35:58.191030Z",
        creator: {
          hasPicture: true,
          id: 46,
          picture: "http://localhost:8080/api/v1/users/46/profile_photo",
          url: "http://localhost:8080/api/v1/users/46",
          username: "GonzaloHirsch",
        },
        deleted: false,
        description: "This is a descripot",
        favorite: true,
        flagged: false,
        id: 184,
        language: {
          deleted: false,
          id: 116,
          name: "sdfsgdg",
          url: "http://localhost:8080/api/v1/languages/116",
        },
        reported: false,
        tags: "http://localhost:8080/api/v1/snippets/184/tags",
        title: "Testing Snippet",
        userReported: false,
        userVotedNegative: false,
        userVotedPositive: false,
        voteCount: 0,
      })
    );
  }),
  rest.get(
    "http://localhost:8080/api/v1/snippets/:id/tags",
    (req, res, ctx) => {
      return res(ctx.json([]));
    }
  ),
  rest.get("http://localhost:8080/api/v1/languages/:id", (req, res, ctx) => {
    return res(
      ctx.json({
        deleted: false,
        id: 116,
        name: "sdfsgdg",
        url: "http://localhost:8080/api/v1/languages/116",
      })
    );
  }),
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
        verified: true,
      })
    );
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

test("renders snippet ok", async () => {
  render(<SnippetOverview />);
});