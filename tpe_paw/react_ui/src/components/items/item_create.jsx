import React, { Component } from "react";
import { ITEM_TYPES } from "../../js/constants";
import ItemCreateForm from "../forms/items_create_form";
import i18n from "../../i18n";
import store from "../../store";

class ItemCreate extends Component {
  constructor(props) {
    super(props);
    const state = store.getState();
    if (state.auth.token === null || state.auth.token === undefined) {
      // ERROR should not be here if you are not logged in
    } else {
      // TODO CHECK THAT HE IS ADMIN
    }
    this.state = { context: ITEM_TYPES.TAG, token: state.auth.token };
  }
  render() {
    return (
      <div className="flex-center">
        <div className="flex-col" style={{ width: "700px" }}>
          <div className="px-3">
            <ul className="nav nav-tabs">
              <li className="nav-item profile-tabs-width">
                <button
                  className={
                    "parent-width nav-link profile-tabs " +
                    (this.state.context !== ITEM_TYPES.TAG
                      ? "fw-300 profile-tabs-unselected"
                      : "fw-500 active")
                  }
                  onClick={() => this.setState({ context: ITEM_TYPES.TAG })}
                >
                  {i18n.t("itemCreate.tag.action")}
                </button>
              </li>
              <li className="nav-item profile-tabs-width">
                <button
                  className={
                    "parent-width nav-link profile-tabs " +
                    (this.state.context !== ITEM_TYPES.LANGUAGE
                      ? "fw-300 profile-tabs-unselected"
                      : "fw-500 active")
                  }
                  onClick={() =>
                    this.setState({ context: ITEM_TYPES.LANGUAGE })
                  }
                >
                  {i18n.t("itemCreate.language.action")}
                </button>
              </li>
            </ul>
          </div>
          <div className="p-3 background-color profile-snippet-container rounded-border">
            <ItemCreateForm {...this.state} />
          </div>
        </div>
      </div>
    );
  }
}

export default ItemCreate;
