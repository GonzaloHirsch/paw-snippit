import React from "react";
import PaginatedItemFeed from "./paginated_item_feed";
import i18n from "../../i18n";
import { ITEM_TYPES } from "../../js/constants";
import { Helmet } from "react-helmet";

// Stateless Functional Component

const LanguagesFeed = (props) => {
  return (
    <React.Fragment>
      <Helmet>
        <title>
          {i18n.t("app")} | {i18n.t("nav.languages")}
        </title>
      </Helmet>
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
        onPageTransitionWithPage={props.onPageTransitionWithPage}
        handleChangeFollowing={props.handleChangeFollowing}
        userIsLogged={props.userIsLogged}
        showFollowing={false}
        loading={props.loading}
        totalItems={props.totalItems}
      />
    </React.Fragment>
  );
};

export default LanguagesFeed;
