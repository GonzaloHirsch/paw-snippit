// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import { renderWithrouter } from "./utils/components";
import ExploreForm from "../components/forms/explore_form";
import { MAX_INTEGER, MIN_INTEGER } from "../js/constants";

// Constants
const tag1 = { id: 1, name: "Tag1", url: "" };
const tag2 = { id: 2, name: "Tag2", url: "" };
const lang1 = { id: 1, name: "Lang1", url: "" };
const lang2 = { id: 2, name: "Lang2", url: "" };

const titlePlaceholder = "Snippet Title";
const usernamePlaceholder = "Username";
const fromPlaceholder = "From";
const toPlaceholder = "To";

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

function checkIfSetOk(placeholder, value, idx) {
  let validPlaceholder =
    idx >= 0
      ? screen.getAllByPlaceholderText(placeholder)[idx]
      : screen.getByPlaceholderText(placeholder);
  fireEvent.change(validPlaceholder, {
    target: { value: value },
  });

  expect(validPlaceholder.value).toBe(value);
}

function checkIfValueOk(placeholder, value, idx) {
  let validPlaceholder =
    idx >= 0
      ? screen.getAllByPlaceholderText(placeholder)[idx]
      : screen.getByPlaceholderText(placeholder);

  expect(validPlaceholder.value).toBe(value);
}

function setValue(placeholder, value, idx) {
  let validPlaceholder =
    idx >= 0
      ? screen.getAllByPlaceholderText(placeholder)[idx]
      : screen.getByPlaceholderText(placeholder);

  fireEvent.change(validPlaceholder, {
    target: { value: value },
  });
}

test("explore title is set ok", async () => {
  renderExploreForm();
  const value = "This is the valid title";
  checkIfSetOk(titlePlaceholder, value, -1);
});

test("explore username is set ok", async () => {
  renderExploreForm();
  const value = "This is the valid username";
  checkIfSetOk(usernamePlaceholder, value, -1);
});

test("explore min reputation is set ok", async () => {
  renderExploreForm();
  const value = "72";
  checkIfSetOk(fromPlaceholder, value, 0);
});

test("explore max reputation is set ok", async () => {
  renderExploreForm();
  const value = "100";
  checkIfSetOk(toPlaceholder, value, 0);
});

test("explore min votes is set ok", async () => {
  renderExploreForm();
  const value = "72";
  checkIfSetOk(fromPlaceholder, value, 1);
});

test("explore max votes is set ok", async () => {
  renderExploreForm();
  const value = "172";
  checkIfSetOk(toPlaceholder, value, 1);
});

// With PROPS
test("explore props are set ok", async () => {
  const urlSearch = {
    field: "date",
    sort: "asc",
    includeFlagged: false,
    title: "Title",
    username: "Username",
    tag: -1,
    language: -1,
    minRep: "4",
    maxRep: "6",
    minDate: null,
    maxDate: null,
    minVotes: "9",
    maxVotes: "100",
  };
  renderWithrouter(<ExploreForm urlSearch={urlSearch} />);

  checkIfValueOk(titlePlaceholder, "Title", -1);
  checkIfValueOk(usernamePlaceholder, "Username", -1);
  checkIfValueOk(fromPlaceholder, "4", 0);
  checkIfValueOk(toPlaceholder, "6", 0);
  checkIfValueOk(fromPlaceholder, "9", 1);
  checkIfValueOk(toPlaceholder, "100", 1);
});

// VALIDATIONS ARE WORKING

// Title too long validation working
test("explore title validations are working", async () => {
  renderExploreForm();
  setValue(
    titlePlaceholder,
    "This is a title that is longer than the maximum 50 characters",
    -1
  );
  fireEvent.click(screen.getByText("Search"));
  expect(
    await screen.queryByText("Title must be less than 50 characters")
  ).toBeInTheDocument();
});

// Username too long validation working
test("explore username validations are working", async () => {
  renderExploreForm();
  setValue(
    usernamePlaceholder,
    "ThisIsAnExampleOfAnExtremelyLongUsernameThatIsInvalid",
    -1
  );
  fireEvent.click(screen.getByText("Search"));

  expect(
    await screen.queryByText("Username must be less than 50 characters")
  ).toBeInTheDocument();
});

// Min reputation is higher than the maximum
test("explore user reputation validations are working", async () => {
  renderExploreForm();
  setValue(fromPlaceholder, "10", 0);
  setValue(toPlaceholder, "6", 0);
  fireEvent.click(screen.getByText("Search"));

  expect(
    await screen.queryByText("From value must be smaller than To value")
  ).toBeInTheDocument();
});

// Min votes is higher than the maximum
test("explore snippet votes validations are working", async () => {
  renderExploreForm();
  setValue(fromPlaceholder, "0", 1);
  setValue(toPlaceholder, "-3", 1);
  fireEvent.click(screen.getByText("Search"));

  expect(
    await screen.queryByText("From value must be smaller than To value")
  ).toBeInTheDocument();
});

// All range validations are working
test("explore range validations are working", async () => {
  renderExploreForm();
  setValue(fromPlaceholder, MAX_INTEGER + 1, 0);
  setValue(toPlaceholder, MIN_INTEGER - 1, 0);
  fireEvent.click(screen.getByText("Search"));

  expect(
    await screen.queryByText("From value must be smaller than To value")
  ).toBeInTheDocument();
  expect(
    await screen.queryByText("Values must be larger than " + MIN_INTEGER)
  ).toBeInTheDocument();
  expect(
    await screen.queryByText("Values must be smaller than " + MAX_INTEGER)
  ).toBeInTheDocument();
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
