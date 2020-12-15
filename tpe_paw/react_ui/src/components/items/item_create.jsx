import React, { Component } from "react";
import { ITEM_TYPES } from "../../js/constants";
import ItemCreateForm from "../forms/items_create_form";
import i18n from "../../i18n";
import store from "../../store";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import { getItemPositionInArrayWithName } from "../../js/item_utils";
import {
  handleChange,
  validateAll,
  hasErrors,
  ITEM_CREATE_VALIDATIONS,
} from "../../js/validations";

class ItemCreate extends Component {
  itemsClient;

  constructor(props) {
    super(props);
    const state = store.getState();
    if (state.auth.token === null || state.auth.token === undefined) {
      // ERROR should not be here if you are not logged in
    } else {
      // TODO CHECK THAT HE IS ADMIN
    }

    this.itemsClient = new LanguagesAndTagsClient(this.props, state.auth.token);
    this.state = {
      context: ITEM_TYPES.TAG,
      itemList: {
        tag: [],
        language: [],
      },
      fields: {
        tag: { item: "" },
        language: { item: "" },
      },
      errors: { tag: { item: null }, language: { item: null } },
      loading: {
        tag: false,
        language: false,
      },
      existsCounter: {
        tag: 0,
        language: 0,
      },
    };
  }

  onAddItemHelper(name, exists, context) {
    console.log("IN HELPER!");
    const items = [...this.state.itemList[context]];
    const itemList = { ...this.state.itemList };
    // Find index of our item
    const i = getItemPositionInArrayWithName(items, name);

    const item = { name: name, exists: exists };
    items.push(item);
    itemList[context] = items;
    this.setState({ itemList: itemList });

    // If a tag or language already exist, want to show an error message
    if (exists) {
      const counters = { ...this.state.existsCounter };
      counters[context] = counters[context] + 1;
      this.setState({ existsCounter: counters });
      console.log(this.state.existsCounter);
    }
  }

  // When a tag or language is added, must check if it
  // already exists and inform the user
  onAddTag = () => {
    const name = this.state.fields.tag.item;

    // Check if the name is already on the list, if it is, do nothing
    if (getItemPositionInArrayWithName(this.state.itemList.tag, name) >= 0) {
      return;
    }
    this.itemsClient
      .getTagExists(name)
      .then((res) => {
        this.onAddItemHelper(name, res.data.aBoolean, ITEM_TYPES.TAG);
      })
      .catch((e) => {});
  };

  onAddLanguage = () => {
    const name = this.state.fields.language.item;

    // Check if the name is already on the list, if it is, do nothing
    if (
      getItemPositionInArrayWithName(this.state.itemList.language, name) >= 0
    ) {
      return;
    }
    this.itemsClient
      .getLanguageExists(name)
      .then((res) => {
        this.onAddItemHelper(name, res.data.aBoolean, ITEM_TYPES.LANGUAGE);
      })
      .catch((e) => {});
  };

  onDeleteTag = (index) => {
    const itemList = { ...this.state.itemList };
    let items = [...this.state.itemList.tag];
    console.log("ITEMS: ", items, index);
    if (items[index].exists) {
      const counters = { ...this.state.existsCounter };
      counters.tag = counters.tag - 1;
      this.setState({ existsCounter: counters });
    }
    items.splice(index, 1);
    itemList.tag = items;
    this.setState({ itemList: itemList });
  };

  onDeleteLanguage = (index) => {
    const itemList = { ...this.state.itemList };
    let items = [...this.state.itemList.language];
    if (items[index].exists) {
      const counters = { ...this.state.existsCounter };
      counters.language = counters.language - 1;
      this.setState({ existsCounter: counters });
    }
    items.splice(index, 1);
    itemList.language = items;
    this.setState({ itemList: itemList });
  };

  // On submit, must call the API itemList.length times
  // to add the corresponding language or tag
  onSubmitTag = () => {
    const filteredList = this.state.itemList.tag.filter((item) => !item.exists);
    const length = filteredList.length;
    const loading = { ...this.state.loading };

    if (length > 0) {
      loading.tag = true;
      this.setState({ loading: loading });
    }

    filteredList.map((item, index) => {
      if (!item.exists) {
        this.itemsClient
          .postAddTag(item.name)
          .then(() => {
            console.log("ADDED A TAG ", item.name);
            loading.tag = index < length - 1;
            this.setState({ loading: loading });
          })
          .catch((e) => {});
      }
    });
  };

  onSubmitLanguage = () => {
    const filteredList = this.state.itemList.language.filter(
      (item) => !item.exists
    );
    const length = filteredList.length;
    const loading = { ...this.state.loading };

    if (length > 0) {
      loading.language = true;
      this.setState({ loading: loading });
    }

    filteredList.map((item, index) => {
      if (!item.exists) {
        this.itemsClient
          .postAddLanguage(item.name)
          .then(() => {
            loading.language = index < length - 1;
            this.setState({ loading: loading });
          })
          .catch((e) => {});
      }
    });
  };

  onInputChange(e, context) {
    console.log("CHANGE ", this.state);
    let fields = { ...this.state.fields };
    let errors = { ...this.state.errors };
    fields[context].item = e.target.value;
    errors[context].item = ITEM_CREATE_VALIDATIONS.item(fields[context].item);

    this.setState({ fields: fields, errors: errors });
  }
  render() {
    const { context } = this.state;
    return (
      <div className="flex-center">
        <div className="flex-col" style={{ width: "700px" }}>
          <div className="px-3">
            <ul className="nav nav-tabs">
              <li className="nav-item profile-tabs-width">
                <button
                  className={
                    "parent-width nav-link profile-tabs " +
                    (context !== ITEM_TYPES.TAG
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
                    (context !== ITEM_TYPES.LANGUAGE
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
          <div className="p-4 background-color profile-snippet-container rounded-border">
            {context === ITEM_TYPES.TAG ? (
              <ItemCreateForm
                context={context}
                value={this.state.fields.tag.item}
                errors={this.state.errors.tag.item}
                itemList={this.state.itemList.tag}
                showExistMsg={this.state.existsCounter.tag > 0}
                onInputChange={(e) => this.onInputChange(e, ITEM_TYPES.TAG)}
                onAdd={() => this.onAddTag()}
                onDelete={(idx) => this.onDeleteTag(idx)}
                onSubmit={() => this.onSubmitTag()}
                loading={this.state.loading.tag}
              />
            ) : (
              <ItemCreateForm
                context={context}
                value={this.state.fields.language.item}
                errors={this.state.errors.language.item}
                itemList={this.state.itemList.language}
                showExistMsg={this.state.existsCounter.language > 0}
                onInputChange={(e) =>
                  this.onInputChange(e, ITEM_TYPES.LANGUAGE)
                }
                onAdd={() => this.onAddLanguage()}
                onDelete={(idx) => this.onDeleteLanguage(idx)}
                onSubmit={() => this.onSubmitLanguage()}
                loading={this.state.loading.language}
              />
            )}
          </div>
        </div>
      </div>
    );
  }
}

export default ItemCreate;
