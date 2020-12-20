// __tests__/fetch.test.js
import React from "react";
import { rest } from "msw";
import { setupServer } from "msw/node";
import { fireEvent, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import { renderWithrouter } from "./utils/components";
import ItemCreate from "../components/items/item_create";

// Constants
const TAG_ACTION_PLACEHOLDER = "Add Tags";
const LANGUAGE_ACTION_PLACEHOLDER = "Add Languages";
const TAG_INPUT_PLACEHOLDER = "Enter the tag to add...";
const LANGUAGE_INPUT_PLACEHOLDER = "Enter the language to add...";
const TAG_NAME = "TAG";
const LANGUAGE_NAME = "LANGUAGE";
const TAG2_NAME = "TAG2";
const LANGUAGE2_NAME = "LANGUAGE2";
const ADD = "Add";

const server = setupServer(
  rest.get(
    "http://localhost:8080/api/v1/tags/exists?name=" + TAG_NAME,
    (req, res, ctx) => {
      return res(ctx.json(false));
    }
  ),
  rest.get(
    "http://localhost:8080/api/v1/tags/exists?name=" + TAG2_NAME,
    (req, res, ctx) => {
      return res(ctx.json(true));
    }
  ),
  rest.get(
    "http://localhost:8080/api/v1/tags/exists?name=" + LANGUAGE_NAME,
    (req, res, ctx) => {
      return res(ctx.json(false));
    }
  ),
  rest.get(
    "http://localhost:8080/api/v1/tags/exists?name=" + LANGUAGE2_NAME,
    (req, res, ctx) => {
      return res(ctx.json(true));
    }
  ),
  rest.post("http://localhost:8080/api/v1/tags", (req, res, ctx) => {
    return res(ctx.json({}));
  }),
  rest.post("http://localhost:8080/api/v1/languages", (req, res, ctx) => {
    return res(ctx.json({}));
  })
);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());

function renderItemCreate() {
  renderWithrouter(<ItemCreate />); // TODO Ver si basta con render()
}

function checkIfSetOk(placeholder, value) {
  let validPlaceholder = screen.getByPlaceholderText(placeholder);
  fireEvent.change(validPlaceholder, {
    target: { value: value },
  });

  expect(validPlaceholder.value).toBe(value);
}

function setValue(placeholder, value) {
  fireEvent.change(screen.getByPlaceholderText(placeholder), {
    target: { value: value },
  });
}

// Renders on Add Tags tab and contains the Add Languages tab
test("item create renders tabs correctly", async () => {
  renderItemCreate();
  expect(screen.queryAllByText(TAG_ACTION_PLACEHOLDER).length).toBe(2); // Tab + Button
  expect(screen.queryAllByText(LANGUAGE_ACTION_PLACEHOLDER).length).toBe(1); // Tab
});

test("item create changes tabs correctly", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));
  expect(screen.queryAllByText(TAG_ACTION_PLACEHOLDER).length).toBe(1); // Tab
  expect(screen.queryAllByText(LANGUAGE_ACTION_PLACEHOLDER).length).toBe(2); // Tab + Button
});

test("item create tag sets values correctly", async () => {
  renderItemCreate();
  checkIfSetOk(TAG_INPUT_PLACEHOLDER, TAG_NAME);
});

test("item create language sets values correctly", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));
  checkIfSetOk(LANGUAGE_INPUT_PLACEHOLDER, LANGUAGE_NAME);
});

test("item create value remains on tab change", async () => {
  renderItemCreate();
  // Set Tag value
  setValue(TAG_INPUT_PLACEHOLDER, TAG_NAME);
  // Change tabs
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));
  // Set language value
  setValue(LANGUAGE_INPUT_PLACEHOLDER, LANGUAGE_NAME);
  // Change tabs
  fireEvent.click(screen.getByText(TAG_ACTION_PLACEHOLDER));
  // Check that Tag value is still there
  expect(screen.getByPlaceholderText(TAG_INPUT_PLACEHOLDER).value).toBe(
    TAG_NAME
  );
  // Change tabs
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));
  // Check that Langugae value is still there
  expect(screen.getByPlaceholderText(LANGUAGE_INPUT_PLACEHOLDER).value).toBe(
    LANGUAGE_NAME
  );
});

test("item create empty add", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(ADD));
  expect(screen.queryByText("1. ")).not.toBeInTheDocument();
});

test("item create adds tags to list", async () => {
  renderItemCreate();
  setValue(TAG_INPUT_PLACEHOLDER, TAG_NAME);
  fireEvent.click(screen.getByText(ADD));
  setValue(TAG_INPUT_PLACEHOLDER, TAG2_NAME);
  fireEvent.click(screen.getByText(ADD));
  expect(await screen.findByText(TAG_NAME)).toBeInTheDocument();
  expect(await screen.findByText(TAG2_NAME)).toBeInTheDocument();
});

// Empty submission shows no alert
test("item create submits tags empty", async () => {
  renderItemCreate();
  fireEvent.click(screen.getAllByText(TAG_ACTION_PLACEHOLDER)[1]);
  expect(
    screen.queryByText("Successfully added the entered tags.")
  ).not.toBeInTheDocument();
  expect(
    screen.queryByText("Something went wrong, some of the tags were not added.")
  ).not.toBeInTheDocument();
});

// Empty submission shows no alert
test("item create submits languages empty", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));
  fireEvent.click(screen.getAllByText(LANGUAGE_ACTION_PLACEHOLDER)[1]);
  expect(
    screen.queryByText("Successfully added the entered tags.")
  ).not.toBeInTheDocument();
  expect(
    screen.queryByText("Something went wrong, some of the tags were not added.")
  ).not.toBeInTheDocument();
});

// VALIDATIONS
test("item create tag spaces validation", async () => {
  renderItemCreate();
  setValue(TAG_INPUT_PLACEHOLDER, "Invalid tag");
  expect(
    screen.queryByText("Value must not contain spaces")
  ).toBeInTheDocument();
});

test("item create tag length validation", async () => {
  renderItemCreate();
  setValue(TAG_INPUT_PLACEHOLDER, "TagIsTooLongAndThisIsInvalidBeware");
  expect(
    screen.queryByText("Value must be between 1 and 30 characters")
  ).toBeInTheDocument();
});

test("item create tag length add validation", async () => {
  renderItemCreate();
  setValue(TAG_INPUT_PLACEHOLDER, "TagIsTooLongAndThisIsInvalidBeware");
  fireEvent.click(screen.getByText(ADD));
  expect(
    screen.queryByText("TagIsTooLongAndThisIsInvalidBeware")
  ).not.toBeInTheDocument();
});

test("item create language spaces validation", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));

  setValue(LANGUAGE_INPUT_PLACEHOLDER, "Invalid lang");
  expect(
    screen.queryByText("Value must not contain spaces")
  ).toBeInTheDocument();
});

test("item create language length validation", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));

  setValue(LANGUAGE_INPUT_PLACEHOLDER, "LanguageIsTooLongAndThisIsInvalid");
  expect(
    screen.queryByText("Value must be between 1 and 30 characters")
  ).toBeInTheDocument();
});

test("item create language length add validation", async () => {
  renderItemCreate();
  fireEvent.click(screen.getByText(LANGUAGE_ACTION_PLACEHOLDER));
  setValue(LANGUAGE_INPUT_PLACEHOLDER, "LanguageIsTooLongAndThisIsInvalid");
  fireEvent.click(screen.getByText(ADD));
  expect(
    screen.queryByText("LanguageIsTooLongAndThisIsInvalid")
  ).not.toBeInTheDocument();
});
