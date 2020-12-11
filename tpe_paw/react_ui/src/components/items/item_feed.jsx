import React, { Component } from "react";
import ItemCard from "./item_card";
import Pagination from "../navigation/pagination";

// Stateless Functional Component

const ItemFeed = (props) => {
  const { items, type } = props;
  return (
    <div className="flex-column">
      <div className="card-columns item-columns">
        {items.map((item) => (
          <ItemCard key={item.id} item={item} type={type} showEmpty={false} userIsLogged={false} showFollowing={false}/>
        ))}
      </div>
    </div>
  );
};

export default ItemFeed;
