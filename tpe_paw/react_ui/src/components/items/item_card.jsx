import React from "react";
import { Link } from "react-router-dom";
import { mdiAlert, mdiHeart, mdiHeartOutline } from "@mdi/js";
import Icon from "@mdi/react";
import { ITEM_TYPES } from "../../js/constants";

const ItemCard = (props) => {
  const { item, type } = props;
  let itemLink;
  if (type === ITEM_TYPES.LANGUAGE) {
    itemLink = `/languages/${item.id}`;
  } else {
    itemLink = `/tags/${item.id}`;
  }
  return (
    <Link to={itemLink} className="no-decoration">
      <div className="card-item-container card bg-light bg-white rounded-border align-items-horizontal-center">
        <div className="card-header no-border px-2" style={{ fontSize: "13px" }}>
          {item.name.toUpperCase()}
        </div>
      </div>
    </Link>
  );
};

export default ItemCard;
