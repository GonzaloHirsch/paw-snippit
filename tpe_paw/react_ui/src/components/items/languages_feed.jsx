import React, { Component } from "react";
import PaginatedItemFeed from "./paginated_item_feed";
import i18n from "../../i18n";
import { ITEM_TYPES } from "../../js/constants";

// Stateless Functional Component

const LanguagesFeed = (props) => {
    console.log(props)
  return (
    <React.Fragment>
      <div className="mx-3 mb-3 fw-100">
        <h1 className="fw-300">{i18n.t("nav.languages")}</h1>
        <h5 className="fw-100">
          ({i18n.t("languagesWithNumber", { count: props.totalItems })})
        </h5>
      </div>
      <PaginatedItemFeed
        items={props.items}
        currentPage={props.currentPage}
        type={ITEM_TYPES.LANGUAGE}
        links={props.links}
        onPageTransition={props.onPageTransition}
        handleChangeFollowing={props.handleChangeFollowing}
        userIsLogged={props.userIsLogged}
        showFollowing={false}
      />
    </React.Fragment>
  );
};

export default LanguagesFeed;
