// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import { renderWithrouter } from "./utils/components";
import SnippetCreateForm from "../components/forms/snippet_create_form";

// Constants
const titlePlaceholder = "Enter your snippet's title...";
const descriptionPlaceholder = "Enter your snippet's description...";
const codePlaceholder = "Enter your snippet's code...";

const server = setupServer(
  rest.post("http://localhost:8080/api/v1/snippets/create", (req, res, ctx) => {
    return res(ctx.json({}));
  }),
  rest.get("http://localhost:8080/api/v1/tags/all", (req, res, ctx) => {
    return res(ctx.json([tag1, tag2]));
  }),
  rest.get("http://localhost:8080/api/v1/languages/all", (req, res, ctx) => {
    return res(ctx.json([lang1, lang2]));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

function renderSnippetCreateForm() {
  renderWithrouter(<SnippetCreateForm />);
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

function setValue(placeholder, value, idx) {
  let validPlaceholder =
    idx >= 0
      ? screen.getAllByPlaceholderText(placeholder)[idx]
      : screen.getByPlaceholderText(placeholder);

  fireEvent.change(validPlaceholder, {
    target: { value: value },
  });
}

test("snippet create title is set ok", async () => {
  renderSnippetCreateForm();
  const value = "This is the valid title";
  checkIfSetOk(titlePlaceholder, value, -1);
});

test("snippet create description is set ok", async () => {
  renderSnippetCreateForm();
  const value = "This is a valid description";
  checkIfSetOk(descriptionPlaceholder, value, -1);
});

test("snippet create code is set ok", async () => {
  renderSnippetCreateForm();
  const value = "This is a valid code";
  checkIfSetOk(codePlaceholder, value, 0);
});

// VALIDATIONS ARE WORKING

// Title too long validation
test("snippet create title length validations are working", async () => {
  renderSnippetCreateForm();
  setValue(
    titlePlaceholder,
    "This is a title that is longer than the maximum 50 characters",
    -1
  );
  expect(
    await screen.queryByText("Title must be less than 50 charaters")
  ).toBeInTheDocument();
});

// Title contains only spaces
test("snippet create title spaces validations are working", async () => {
  renderSnippetCreateForm();
  setValue(titlePlaceholder, "          ", -1);
  expect(
    await screen.queryByText("Title can not be empty")
  ).toBeInTheDocument();
});

// Description too long validation working
test("snippet create description validations are working", async () => {
  renderSnippetCreateForm();
  setValue(
    descriptionPlaceholder,
    "Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. Will repeat this text until it surpasses 500 characters. ",
    -1
  );
  expect(
    await screen.queryByText("Description must be less than 500 charaters")
  ).toBeInTheDocument();
});

// Code too short validation working
test("snippet create code validations are working", async () => {
  renderSnippetCreateForm();
  setValue(codePlaceholder, "      Code    ", -1);
  expect(
    await screen.queryByText("Code must include more than 5 charaters")
  ).toBeInTheDocument();
});

// CREATING SNIPPET WITH ERRORS
test("snippet create with errors not allowed", async () => {
  renderSnippetCreateForm();
  fireEvent.click(screen.getByText("Create Snippet"));

  expect(
    await screen.queryByText("Title can not be empty")
  ).toBeInTheDocument();
  expect(await screen.queryByText("Code can not be empty")).toBeInTheDocument();
  expect(
    await screen.queryByText("Must select a language")
  ).toBeInTheDocument();
});
