import React from "react";
import ItemCard from "./item_card";
import Pagination from "../navigation/pagination";

// Stateless Functional Component

const PaginatedItemFeed = (props) => {
  const {
    items,
    currentPage,
    type,
    links,
    onPageTransition,
    userIsLogged,
    showFollowing,
    handleChangeFollowing,
  } = props;
  return (
    <div className="flex-column px-4 pb-4 pt-2">
      <div className="card-columns feed-item-columns px-2">
        {items.map((item) => (
          <ItemCard
            key={item.id}
            item={item}
            type={type}
            userIsLogged={userIsLogged}
            showEmpty={true}
            showFollowing={showFollowing}
            onChangeFollowing={handleChangeFollowing}
          />
        ))}
      </div>
      <div className="flex-center mt-4">
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

export default PaginatedItemFeed;
