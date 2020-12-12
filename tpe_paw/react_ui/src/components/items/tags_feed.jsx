import React from "react";
import PaginatedItemFeed from "./paginated_item_feed";
import i18n from "../../i18n";
import { ITEM_TYPES } from "../../js/constants";

// Stateless Functional Component

const TagsFeed = (props) => {
  console.log(props);
  return (
    <React.Fragment>
      <div className="mx-3 mb-3 fw-100">
        <h1 className="fw-300">{i18n.t("nav.tags")}</h1>
        <h5 className="fw-100">
          ({i18n.t("tagsWithNumber", { count: props.totalItems })})
        </h5>
      </div>
      <PaginatedItemFeed
        items={props.items}
        currentPage={props.currentPage}
        type={ITEM_TYPES.TAG}
        links={props.links}
        onPageTransition={props.onPageTransition}
        handleChangeFollowing={props.handleChangeFollowing}
        userIsLogged={props.userIsLogged}
        showFollowing={true}
      />
    </React.Fragment>
  );
};

export default TagsFeed;
