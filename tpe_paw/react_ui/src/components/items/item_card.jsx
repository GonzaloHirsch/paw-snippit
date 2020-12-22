import React, { Component } from "react";
import { Link } from "react-router-dom";
import { mdiCancel } from "@mdi/js";
import Icon from "@mdi/react";
import { ITEM_TYPES } from "../../js/constants";
import i18n from "../../i18n";
import { Tooltip, Badge } from "reactstrap";

class ItemCard extends Component {
  state = { tooltipOpen: false };

  render() {
    const {
      item,
      type,
      showEmpty,
      showFollowing,
      onChangeFollowing,
      userIsLogged,
    } = this.props;
    let itemLink;
    if (type === ITEM_TYPES.LANGUAGE) {
      itemLink = `/languages/${item.id}`;
    } else {
      itemLink = `/tags/${item.id}`;
    }
    return (
      <Link to={itemLink} className="no-decoration">
        <div className="card-item-container card bg-light bg-white rounded-border align-items-horizontal-center">
          <div
            className="card-header item-card-header row justify-content-end no-border no-margin px-2 align-items-vertical"
            style={{ fontSize: "13px" }}
          >
            <div
              className={
                (showEmpty ? "align-items-horizontal-left col-8" : "col-12")
              }
            >
              {item.name.toUpperCase()}
            </div>
            {showEmpty && (
              <div className="col-4 no-padding align-items-horizontal-right">
                {showEmpty && item.empty && (
                  <React.Fragment>
                    <Icon
                      id="emptyIcon"
                      className="icon-size-1"
                      path={mdiCancel}
                      size={1}
                    />
                    <Tooltip
                      placement="right"
                      isOpen={this.state.tooltipOpen}
                      target="emptyIcon"
                      toggle={() =>
                        this.setState({ tooltipOpen: !this.state.tooltipOpen })
                      }
                    >
                      {i18n.t("items.empty")}
                    </Tooltip>
                  </React.Fragment>
                )}
                {userIsLogged && showFollowing && (
                    <Badge
                      onClick={(e) => onChangeFollowing(e, item.id)}
                      className={
                        "tag-action-badge " + (item.empty ? "ml-1" : "")
                      }
                      color="secondary"
                    >
                      {item.following
                        ? i18n.t("items.unfollow")
                        : i18n.t("items.follow")}
                    </Badge>
                )}
              </div>
            )}
          </div>
        </div>
      </Link>
    );
  }
}

export default ItemCard;
