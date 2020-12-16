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
import { Helmet } from "react-helmet";


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
    };
  }

  onAddItemHelper(name, exists, context) {
    const items = [...this.state.itemList[context]];
    const itemList = { ...this.state.itemList };
    // Find index of our item
    const i = getItemPositionInArrayWithName(items, name);

    const item = { name: name, exists: exists };
    items.push(item);
    itemList[context] = items;
    this.setState({ itemList: itemList });
  }

  // When a tag or language is added, must check if it
  // already exists and inform the user
  onAddTag = () => {
    if (this.state.errors.tag.item !== null) return;
    const fields = { ...this.state.fields };
    const name = fields.tag.item;

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

    // Clear the input
    fields.tag.item = "";
    this.setState({ fields: fields });
  };

  onAddLanguage = () => {
    if (this.state.errors.language.item !== null) return;
    const fields = { ...this.state.fields };
    const name = fields.language.item;

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

    // Clear the input
    fields.language.item = "";
    this.setState({ fields: fields });
  };

  onDeleteTag = (index) => {
    const itemList = { ...this.state.itemList };
    let items = [...this.state.itemList.tag];
    items.splice(index, 1);
    itemList.tag = items;
    this.setState({ itemList: itemList });
  };

  onDeleteLanguage = (index) => {
    const itemList = { ...this.state.itemList };
    let items = [...this.state.itemList.language];
    items.splice(index, 1);
    itemList.language = items;
    this.setState({ itemList: itemList });
  };

  _onSubmitStateUpdate(context) {
    const loading = { ...this.state.loading };
    const itemList = { ...this.state.itemList };
    loading[context] = false;
    itemList[context] = [];
    this.setState({ itemList: itemList, loading: loading });
    // TODO -> ALERT!
  }

  // On submit, must call the API itemList.length times
  // to add the corresponding language or tag
  onSubmitTag = () => {
    const filteredList = this.state.itemList.tag.filter((item) => !item.exists);
    const length = filteredList.length;
    const loading = { ...this.state.loading };

    if (length > 0) {
      loading.tag = true;
      this.setState({ loading: loading });
    } else {
      this._onSubmitStateUpdate(ITEM_TYPES.TAG);
    }

    filteredList.map((item, index) => {
      if (!item.exists) {
        this.itemsClient
          .postAddTag(item.name)
          .then(() => {
            // Submitted the last item
            if (!(index < length - 1)) {
              this._onSubmitStateUpdate(ITEM_TYPES.TAG);
            }
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
    } else {
      this._onSubmitStateUpdate(ITEM_TYPES.LANGUAGE);
    }

    filteredList.map((item, index) => {
      if (!item.exists) {
        this.itemsClient
          .postAddLanguage(item.name)
          .then(() => {
            // Submitted the last item
            if (!(index < length - 1)) {
              this._onSubmitStateUpdate(ITEM_TYPES.LANGUAGE);
            }
          })
          .catch((e) => {});
      }
    });
  };

  onInputChange(e, context) {
    let fields = { ...this.state.fields };
    let errors = { ...this.state.errors };
    fields[context].item = e.target.value;
    errors[context].item = ITEM_CREATE_VALIDATIONS.item(fields[context].item);

    this.setState({ fields: fields, errors: errors });
  }
  render() {
    const { context } = this.state;
    return (
      <div className="flex-center flex-col">
        <Helmet>
          <title>
            {i18n.t("app")} | {i18n.t("itemCreate.header")}
          </title>
        </Helmet>
        <h1 className="fw-100 my-3">{i18n.t("itemCreate.header")}</h1>

        <div className="flex-col mt-2 shadow" style={{ width: "660px" }}>
          <div>
            <ul className="nav nav-tabs">
              <li className="nav-item profile-tabs-width">
                <button
                  className={
                    "parent-width nav-link profile-tabs " +
                    (context !== ITEM_TYPES.TAG
                      ? "fw-100 item-tabs-unselected"
                      : "fw-300 active")
                  }
                  style={{ fontSize: "30px" }}
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
                      ? "fw-100 item-tabs-unselected"
                      : "fw-300 active")
                  }
                  style={{ fontSize: "30px" }}
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
