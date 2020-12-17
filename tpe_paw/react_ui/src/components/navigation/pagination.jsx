import React, { Component } from "react";

import Icon from "@mdi/react";
import {
  mdiChevronDoubleLeft,
  mdiChevronDoubleRight,
  mdiChevronLeft,
  mdiChevronRight,
} from "@mdi/js";

class Pagination extends Component {
  NEXT = "next";
  FIRST = "first";
  PREV = "prev";
  LAST = "last";
  LIMIT_PER_SIDE = 3;
  state = {};

  getPageNumberFromLink(link) {
    return parseInt(new URLSearchParams(link.split("?")[1]).get("page"), 10);
  }

  sortPagesArray(arr) {
    arr = arr.sort(function (a, b) {
      return a - b;
    });
    return arr;
  }

  getNumberLeftItems(links) {
    let limit = 1;
    if (links[this.FIRST] !== undefined) {
      limit = 1;
    } else if (links[this.PREV] !== undefined) {
      limit = this.getPageNumberFromLink(links[this.PREV].url);
    }
    let count = 0;
    let pages = [];
    for (
      let i = this.props.currentPage - 1;
      i >= limit && count < this.LIMIT_PER_SIDE;
      i--
    ) {
      pages.push(i);
      count += 1;
    }
    return this.sortPagesArray(pages);
  }

  getNumberRightItems(links) {
    let limit = this.props.currentPage;
    if (links[this.LAST] !== undefined) {
      limit = this.getPageNumberFromLink(links[this.LAST].url);
    } else if (links[this.NEXT] !== undefined) {
      limit = this.getPageNumberFromLink(links[this.NEXT].url);
    }
    let count = 0;
    let pages = [];
    for (
      let i = this.props.currentPage + 1;
      i <= limit && count < this.LIMIT_PER_SIDE;
      i++
    ) {
      pages.push(i);
      count += 1;
    }
    return this.sortPagesArray(pages);
  }

  renderPageItem(i, handler){
    return (<li
      className="page-item page-item-number"
      onClick={() => handler(i)}
    >
      <span
        className="page-link"
        style={{
          height: "100%",
          display: "flex",
          alignItems: "center",
          cursor: "pointer",
        }}
      >
        {i}
      </span>
    </li>);
  }

  render() {
    const curr = this.props.currentPage;
    const links = this.props.links;
    console.log(links, curr, "PAG")
    const handlePageTransition = this.props.onPageTransition;
    const handlePageTransitionWithPage = this.props.onPageTransitionWithPage;
    const leftPages = this.getNumberLeftItems(links);
    const rightPages = this.getNumberRightItems(links);
    return (
      <nav>
        <ul className="pagination">
          {links[this.FIRST] !== undefined && (
            <li
              className="page-item"
              onClick={() => handlePageTransition(this.FIRST)}
            >
              <span className="page-link" aria-label="First">
                <Icon aria-hidden="true" path={mdiChevronDoubleLeft} size={1} />{" "}
              </span>
            </li>
          )}
          {links[this.PREV] !== undefined && (
            <li
              className="page-item"
              onClick={() => handlePageTransition(this.PREV)}
            >
              <span className="page-link" aria-label="Previous">
                <Icon aria-hidden="true" path={mdiChevronLeft} size={1} />
              </span>
            </li>
          )}
          {leftPages.map((i) => (
            this.renderPageItem(i, handlePageTransitionWithPage)
          ))}
          <li className="page-item page-item-number">
            <span
              className="page-link page-link-selected"
              style={{
                height: "100%",
                display: "flex",
                alignItems: "center",
                cursor: "pointer",
              }}
            >
              {curr}
            </span>
          </li>
          {rightPages.map((i) => (
            this.renderPageItem(i, handlePageTransitionWithPage)
          ))}
          {links[this.NEXT] !== undefined && (
            <li
              className="page-item"
              onClick={() => handlePageTransition(this.NEXT)}
            >
              <span className="page-link" aria-label="Next">
                <Icon aria-hidden="true" path={mdiChevronRight} size={1} />
              </span>
            </li>
          )}
          {links[this.LAST] !== undefined && (
            <li
              className="page-item"
              onClick={() => handlePageTransition(this.LAST)}
            >
              <span className="page-link" aria-label="Last">
                <Icon
                  aria-hidden="true"
                  path={mdiChevronDoubleRight}
                  size={1}
                />
              </span>
            </li>
          )}
        </ul>
      </nav>
    );
  }
}

export default Pagination;
