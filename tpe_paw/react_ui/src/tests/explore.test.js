// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import { renderWithrouter } from "./utils/components";
import { getDateFromString } from "../js/date_utils";
import ExploreForm from "../components/forms/explore_form";

const tag1 = { id: 1, name: "Tag1", url: "" };
const tag2 = { id: 2, name: "Tag2", url: "" };
const lang1 = { id: 1, name: "Lang1", url: "" };
const lang2 = { id: 2, name: "Lang2", url: "" };

const server = setupServer(
  rest.get("http://localhost:8080/api/v1/tags/all", (req, res, ctx) => {
    return res(ctx.json([tag1, tag2]));
  }),
  rest.get("http://localhost:8080/api/v1/languages/all", (req, res, ctx) => {
    return res(ctx.json([lang1, lang2]));
  }),
  rest.get("http://localhost:8080/api/v1/languages/1", (req, res, ctx) => {
    return res(ctx.json(lang1));
  }),
  rest.get("http://localhost:8080/api/v1/tags/2", (req, res, ctx) => {
    return res(ctx.json(tag2));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

function renderExploreForm() {
  const urlSearch = {
    field: null,
    sort: null,
    includeFlagged: null,
    title: null,
    username: null,
    tag: -1,
    language: -1,
    minRep: null,
    maxRep: null,
    minDate: null,
    maxDate: null,
    minVotes: null,
    maxVotes: null,
  };
  renderWithrouter(<ExploreForm urlSearch={urlSearch} />);
}

function checkIfValueOk(placeholder, value, idx) {
  renderExploreForm();

  let validPlaceholder =
    idx >= 0
      ? screen.getAllByPlaceholderText(placeholder)[idx]
      : screen.getByPlaceholderText(placeholder);
  fireEvent.change(validPlaceholder, {
    target: { value: value },
  });

  expect(validPlaceholder.value).toBe(value);
}

test("explore title is set ok", async () => {
  const value = "This is the valid title";
  const placeholder = "Snippet Title";
  checkIfValueOk(placeholder, value, -1);
});

test("explore username is set ok", async () => {
  const value = "This is the valid username";
  const placeholder = "Username";
  checkIfValueOk(placeholder, value, -1);
});

test("explore min reputation is set ok", async () => {
  const value = "72";
  const placeholder = "From";
  checkIfValueOk(placeholder, value, 0);
});

test("explore max reputation is set ok", async () => {
  const value = "100";
  const placeholder = "To";
  checkIfValueOk(placeholder, value, 0);
});

test("explore min votes is set ok", async () => {
  const value = "72";
  const placeholder = "From";
  checkIfValueOk(placeholder, value, 1);
});

test("explore max votes is set ok", async () => {
  const value = "172";
  const placeholder = "To";
  checkIfValueOk(placeholder, value, 1);
});

// test("shows required errors in login", async () => {
//   renderWithrouter(<Login />);

//   fireEvent.click(screen.getByText("Log In"));

//   expect(screen.getByText("User is required")).toBeInTheDocument();
//   expect(screen.getByText("Password is required")).toBeInTheDocument();
// });
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
