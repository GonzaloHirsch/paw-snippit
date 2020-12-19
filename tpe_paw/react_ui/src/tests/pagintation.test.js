// __tests__/fetch.test.js
import React from "react";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import Pagination from "../components/navigation/pagination";

test("shows only page 1", async () => {
  render(
    <Pagination
      currentPage={1}
      links={{}}
      onPageTransition={() => {}}
      onPageTransitionWithPage={() => {}}
    />
  );

  // Number items
  expect(screen.queryByText("1")).toBeInTheDocument();
  expect(screen.queryByText("2")).not.toBeInTheDocument();

  // Arrows
  expect(screen.queryByLabelText("First")).not.toBeInTheDocument();
  expect(screen.queryByLabelText("Previous")).not.toBeInTheDocument();
  expect(screen.queryByLabelText("Next")).not.toBeInTheDocument();
  expect(screen.queryByLabelText("Last")).not.toBeInTheDocument();
});

test("shows up to 3 pages each side", async () => {
  render(
    <Pagination
      currentPage={4}
      links={{
        first: { url: "http://localhost:8080/api/v1/snippets?page=1" },
        prev: { url: "http://localhost:8080/api/v1/snippets?page=3" },
        next: { url: "http://localhost:8080/api/v1/snippets?page=5" },
        last: { url: "http://localhost:8080/api/v1/snippets?page=7" },
      }}
      onPageTransition={() => {}}
      onPageTransitionWithPage={() => {}}
    />
  );

  // Number items
  expect(screen.queryByText("1")).toBeInTheDocument();
  expect(screen.queryByText("2")).toBeInTheDocument();
  expect(screen.queryByText("3")).toBeInTheDocument();
  expect(screen.queryByText("4")).toBeInTheDocument();
  expect(screen.queryByText("5")).toBeInTheDocument();
  expect(screen.queryByText("6")).toBeInTheDocument();
  expect(screen.queryByText("7")).toBeInTheDocument();

  // Arrows
  expect(screen.queryByLabelText("First")).toBeInTheDocument();
  expect(screen.queryByLabelText("Previous")).toBeInTheDocument();
  expect(screen.queryByLabelText("Next")).toBeInTheDocument();
  expect(screen.queryByLabelText("Last")).toBeInTheDocument();
});

test("shows up to 2 pages each side", async () => {
  render(
    <Pagination
      currentPage={3}
      links={{
        first: { url: "http://localhost:8080/api/v1/snippets?page=1" },
        prev: { url: "http://localhost:8080/api/v1/snippets?page=2" },
        next: { url: "http://localhost:8080/api/v1/snippets?page=4" },
        last: { url: "http://localhost:8080/api/v1/snippets?page=5" },
      }}
      onPageTransition={() => {}}
      onPageTransitionWithPage={() => {}}
    />
  );

  // Number items
  expect(screen.queryByText("1")).toBeInTheDocument();
  expect(screen.queryByText("2")).toBeInTheDocument();
  expect(screen.queryByText("3")).toBeInTheDocument();
  expect(screen.queryByText("4")).toBeInTheDocument();
  expect(screen.queryByText("5")).toBeInTheDocument();

  // Arrows
  expect(screen.queryByLabelText("First")).toBeInTheDocument();
  expect(screen.queryByLabelText("Previous")).toBeInTheDocument();
  expect(screen.queryByLabelText("Next")).toBeInTheDocument();
  expect(screen.queryByLabelText("Last")).toBeInTheDocument();
});

test("shows up to 1 page each side", async () => {
  render(
    <Pagination
      currentPage={2}
      links={{
        prev: { url: "http://localhost:8080/api/v1/snippets?page=1" },
        next: { url: "http://localhost:8080/api/v1/snippets?page=3" },
      }}
      onPageTransition={() => {}}
      onPageTransitionWithPage={() => {}}
    />
  );

  // Number items
  expect(screen.queryByText("1")).toBeInTheDocument();
  expect(screen.queryByText("2")).toBeInTheDocument();
  expect(screen.queryByText("3")).toBeInTheDocument();

  // Arrows
  expect(screen.queryByLabelText("First")).not.toBeInTheDocument();
  expect(screen.queryByLabelText("Previous")).toBeInTheDocument();
  expect(screen.queryByLabelText("Next")).toBeInTheDocument();
  expect(screen.queryByLabelText("Last")).not.toBeInTheDocument();
});

test("shows 3 pages on right, none on left", async () => {
  render(
    <Pagination
      currentPage={1}
      links={{
        next: { url: "http://localhost:8080/api/v1/snippets?page=2" },
        last: { url: "http://localhost:8080/api/v1/snippets?page=4" },
      }}
      onPageTransition={() => {}}
      onPageTransitionWithPage={() => {}}
    />
  );

  // Number items
  expect(screen.queryByText("1")).toBeInTheDocument();
  expect(screen.queryByText("2")).toBeInTheDocument();
  expect(screen.queryByText("3")).toBeInTheDocument();
  expect(screen.queryByText("4")).toBeInTheDocument();

  // Arrows
  expect(screen.queryByLabelText("First")).not.toBeInTheDocument();
  expect(screen.queryByLabelText("Previous")).not.toBeInTheDocument();
  expect(screen.queryByLabelText("Next")).toBeInTheDocument();
  expect(screen.queryByLabelText("Last")).toBeInTheDocument();
});

test("shows 3 pages on left, none on right", async () => {
    render(
      <Pagination
        currentPage={4}
        links={{
          first: { url: "http://localhost:8080/api/v1/snippets?page=1" },
          prev: { url: "http://localhost:8080/api/v1/snippets?page=3" },
        }}
        onPageTransition={() => {}}
        onPageTransitionWithPage={() => {}}
      />
    );
  
    // Number items
    expect(screen.queryByText("1")).toBeInTheDocument();
    expect(screen.queryByText("2")).toBeInTheDocument();
    expect(screen.queryByText("3")).toBeInTheDocument();
    expect(screen.queryByText("4")).toBeInTheDocument();
  
    // Arrows
    expect(screen.queryByLabelText("First")).toBeInTheDocument();
    expect(screen.queryByLabelText("Previous")).toBeInTheDocument();
    expect(screen.queryByLabelText("Next")).not.toBeInTheDocument();
    expect(screen.queryByLabelText("Last")).not.toBeInTheDocument();
  });
  
