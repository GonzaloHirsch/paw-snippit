import React, { Component } from "react";
import i18n from "../../i18n";
import ItemListElement from "../items/item_list_element";
import { mdiLoading } from "@mdi/js";

class ItemCreateForm extends Component {
  _renderSubmitButton() {
    return (
      <button
        className={
          "btn btn-lg btn-primary btn-block mt-3 rounded-border form-button ld-over-inverse " +
          (this.props.loading ? "running" : "")
        }
        type="submit"
      >
        <div className="ld ld-ring ld-spin"></div>
        {i18n.t("itemCreate." + this.props.context + ".action")}
      </button>
    );
  }

  render() {
    const {
      context,
      value,
      errors,
      itemList,
      showExistsMsg,
      onSubmit,
      onAdd,
      onDelete,
      onInputChange,
    } = this.props;
    const itemName = i18n.t("itemCreate." + context + ".name");
    const itemNamePlural = i18n.t("itemCreate." + context + ".name_plural");
    console.log(this.props);
    return (
      <form onSubmit={() => onSubmit()}>
        <div className="d-flex justify-space-between">
          <input
            id="createItemInput"
            type="text"
            className={"form-control  " + (errors && "with-error")}
            placeholder={i18n.t("itemCreate.placeholders.item", {
              item: itemName,
            })}
            onChange={(e) => onInputChange(e)}
            value={value}
          />
          {errors && (
            <span className="text-danger item-value-error">{errors}</span>
          )}
          <div
            className="btn btn-success ml-3 rounded-border"
            onClick={() => onAdd()}
          >
            {i18n.t("itemCreate.add")}
          </div>
        </div>
        <h6 className="mt-4">
          {i18n.t("itemCreate.indications", { items: itemNamePlural })}
        </h6>
        <div className="item-list-container rounded-border">
          {itemList.map((item, i) => (
            <ItemListElement
              key={item.name}
              index={i + 1}
              item={item}
              onDelete={(i) => onDelete(i)}
            />
          ))}
        </div>
        {showExistsMsg ? (
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
