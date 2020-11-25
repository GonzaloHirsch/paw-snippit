import React, { Component } from "react";
import { Link } from "react-router-dom";

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
  state = {};

  render() {
    const curr = this.props.currentPage;
    const links = this.props.links;
    const handlePageTransition = this.props.onPageTransition;
    return (
      <nav >
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
          <li className="page-item">
            <span
              className="page-link"
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
