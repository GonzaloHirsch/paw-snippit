import React from "react";
import ItemCard from "./item_card";
import Pagination from "../navigation/pagination";
import { ITEM_TYPES } from "../../js/constants";
import i18n from "../../i18n";
import { Spinner } from "reactstrap";

// Stateless Functional Component

const PaginatedItemFeed = (props) => {
  const {
    items,
    currentPage,
    type,
    links,
    onPageTransition,
    onPageTransitionWithPage,
    userIsLogged,
    showFollowing,
    handleChangeFollowing,
    loading,
    totalItems,
  } = props;
  return (
    <div className="flex-column px-4 pb-4 pt-2">
      {loading && (
        <div className="align-items-vertical align-items-horizontal-center">
          <Spinner color="dark" style={{ width: "3rem", height: "3rem" }} />
        </div>
      )}
      {!loading &&
        (totalItems === 0 ? (
          <div
            className="flex-center fw-100 text-center"
            style={{ fontSize: "50px" }}
          >
            {i18n.t("itemsEmpty", {
              items:
                type === ITEM_TYPES.LANGUAGE
                  ? i18n.t("languagesLower", { count: 0})
                  : i18n.t("tagsLower", { count: 0 }),
            })}
          </div>
        ) : (
          <React.Fragment>
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
                onPageTransitionWithPage={onPageTransitionWithPage}
                className="mx-auto"
              />
            </div>
          </React.Fragment>
        ))}
    </div>
  );
};

export default PaginatedItemFeed;
