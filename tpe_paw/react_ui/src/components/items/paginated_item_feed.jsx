import React, { Component } from "react";
import ItemCard from "./item_card";
import Pagination from "../navigation/pagination";

// Stateless Functional Component

const Paginated = (props) => {
  const { items, currentPage, type } = props;
  return (
    <div className="flex-column">
      <div className="card-columns">
        {items.map((item) => (
          <ItemCard key={item.id} item={item} type={type} />
        ))}
      </div>
      <div className="flex-center">
          <Pagination
            currentPage={currentPage}
            links={links}
            onPageTransition={onPageTransition}
            className="mx-auto"
          />
        </div>
    </div>
  );
};

export default Paginated;
