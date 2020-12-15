import React, { Component } from "react";
import store from "../../store";
import i18n from "../../i18n";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import { ITEM_TYPES } from "../../js/constants";
import { getItemPositionInArrayWithName } from "../../js/item_utils";

class ItemCreateForm extends Component {
  itemsClient;

  constructor(props) {
    super(props);
    const state = store.getState();
    if (state.auth.token === null || state.auth.token === undefined) {
      // ERROR should not be here if you are not logged in
    } else {
      // TODO CHECK THAT HE IS ADMIN
    }
    this.itemsClient = new LanguagesAndTagsClient(this.props, this.props.token);
    this.state = {
      itemList: [],
    };
  }

  _onAddItemHelper(name, exists) {
    let items = [...this.state.itemList];
    // Find index of our item
    const i = getItemPositionInArrayWithName(items, name);
    // Add only if it does not exists
    if (i === -1) {
      const item = { name: name, error: exists };
      items.push(item);
      this.setState({ itemList: items });
    }
  }

  // When a tag or language is added, must check if it
  // already exists and inform the user
  _onAddItem(name) {
    if (this.props.context === ITEM_TYPES.LANGUAGE) {
      this.itemsClient
        .getLanguageExists(name)
        .then((res) => {
          this._onAddItemHelper(name, res.data.aBoolean);
        })
        .error((e) => {});
    } else {
      this.itemsClient
        .getTagExists(name)
        .then((res) => {
          this._onAddItemHelper(name, res.data.aBoolean);
        })
        .error((e) => {});
    }
  }

  // On submit, must call the API itemList.length times
  // to add the corresponding language or tag
  _onSubmit() {
    const { itemList } = this.state;
    if (this.props.context === ITEM_TYPES.LANGUAGE) {
      itemList.map((i) =>
        this.itemsClient
          .postAddLanguage(i.name)
          .then(() => {})
          .error((e) => {})
      );
    } else {
      itemList.map((i) =>
        this.itemsClient
          .postAddTag(i.name)
          .then(() => {})
          .error((e) => {})
      );
    }
  }

  render() {
    return (
      <div>{this.props.context === ITEM_TYPES.TAG ? "TAG" : "LANGUAGE"}</div>
    );
  }
}

export default ItemCreateForm;
