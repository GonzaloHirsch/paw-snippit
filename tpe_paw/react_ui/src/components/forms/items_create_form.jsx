import React, { Component } from "react";
import i18n from "../../i18n";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import { ITEM_TYPES } from "../../js/constants";
import { getItemPositionInArrayWithName } from "../../js/item_utils";
import { mdiLoading } from "@mdi/js";
import {
  handleChange,
  validateAll,
  hasErrors,
  ITEM_CREATE_VALIDATIONS,
} from "../../js/validations";
import ItemListElement from "../items/item_list_element";

class ItemCreateForm extends Component {
  itemsClient;

  constructor(props) {
    super(props);
    this.itemsClient = new LanguagesAndTagsClient(this.props, this.props.token);
    this.state = {
      itemList: [],
      fields: {
        item: "",
      },
      errors: { item: null },
      loading: false,
      existsCounter: 0,
    };
  }

  _onAddItemHelper(name, exists) {
    let items = [...this.state.itemList];
    // Find index of our item
    const i = getItemPositionInArrayWithName(items, name);
    // Add only if it does not exists
    if (i === -1) {
      const item = { name: name, exists: exists };
      items.push(item);
      this.setState({ itemList: items });
    }

    // If a tag or language already exist, want to show an error message
    if (exists) {
      this.setState({ existsCounter: this.state.existsCounter + 1 });
    }
  }

  // When a tag or language is added, must check if it
  // already exists and inform the user
  _onAddItem() {
    const name = this.state.fields.item;

    // Check if the name is already on the list, if it is, do nothing
    if (getItemPositionInArrayWithName(this.state.itemList, name) >= 0) {
      return;
    }

    // If it is not, add it
    if (this.props.context === ITEM_TYPES.LANGUAGE) {
      this.itemsClient
        .getLanguageExists(name)
        .then((res) => {
          this._onAddItemHelper(name, res.data.aBoolean);
        })
        .catch((e) => {});
    } else {
      this.itemsClient
        .getTagExists(name)
        .then((res) => {
          this._onAddItemHelper(name, res.data.aBoolean);
        })
        .catch((e) => {});
    }
  }

  onDeleteItem = (index) => {
    let items = [...this.state.itemList];
    if (items[index].exists) {
      this.setState({ existsCounter: this.state.existsCounter - 1 });
    }
    items.splice(index, 1);
    this.setState({ itemList: items });
  };

  // On submit, must call the API itemList.length times
  // to add the corresponding language or tag
  onSubmit = () => {
    const filteredList = this.state.itemList.filter((item) => !item.exists);
    const length = filteredList.length;
    console.log("LENGTH: ", length);
    if (length > 0) {
      this.setState({ loading: true });
    }

    if (this.props.context === ITEM_TYPES.LANGUAGE) {
      filteredList.map((item, index) => {
        if (!item.exists) {
          this.itemsClient
            .postAddLanguage(item.name)
            .then(() => {
              this.setState({ loading: index < length - 1 });
            })
            .catch((e) => {});
        }
      });
    } else {
      filteredList.map((item, index) => {
        if (!item.exists) {
          this.itemsClient
            .postAddTag(item.name)
            .then(() => {
              console.log("ADDED A TAG ", item.name);
              this.setState({ loading: index < length - 1 });
            })
            .catch((e) => {});
        }
      });
    }
  };

  _onInputChange(e) {
    const state = handleChange(
      e,
      "item",
      ITEM_CREATE_VALIDATIONS,
      this.state.fields,
      this.state.errors
    );
    this.setState(state);
  }

  _renderSubmitButton() {
    return (
      <button
        className={
          "btn btn-lg btn-primary btn-block mt-3 rounded-border form-button ld-over-inverse " +
          (this.state.loading ? "running" : "")
        }
        type="submit"
      >
        <div className="ld ld-ring ld-spin"></div>
        {i18n.t("itemCreate." + this.props.context + ".action")}
      </button>
    );
  }

  render() {
    const { fields, errors } = this.state;
    const itemName = i18n.t("itemCreate." + this.props.context + ".name");
    return (
      <form onSubmit={() => this.onSubmit()}>
        <div className="d-flex justify-space-between">
          <input
            id="createItemInput"
            type="text"
            className={"form-control  " + (errors.item && "with-error")}
            placeholder={i18n.t("itemCreate.placeholders.item", {
              item: itemName,
            })}
            onChange={(e) => this._onInputChange(e)}
            value={fields.item}
          />
          {errors.item && (
            <span className="text-danger item-value-error">{errors.item}</span>
          )}
          <div
            className="btn btn-success ml-3 rounded-border"
            onClick={() => this._onAddItem()}
          >
            {i18n.t("itemCreate.add")}
          </div>
        </div>
        <h6 className="mt-4">List of tags to add will appear below</h6>
        <div className="item-list-container rounded-border">
          {this.state.itemList.map((item, i) => (
            <ItemListElement
              key={item.name}
              index={i + 1}
              item={item}
              onDelete={(i) => this.onDeleteItem(i)}
            />
          ))}
        </div>
        {this.state.existsCounter > 0 ? (
          <div className="flex-center item-element-exists-msg">
            {i18n.t("itemCreate.existsMsg")}
          </div>
        ) : null}
        {this._renderSubmitButton()}
      </form>
    );
  }
}

export default ItemCreateForm;
