import React from "react";
import { ITEM_TYPES } from "../../js/constants";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import i18n from "../../i18n";


const DeleteItemModal = (props) => {
  const { item, type, isOpen, onToggle, handleDeleteConfirm } = props;
  return (
    <Modal isOpen={isOpen} toggle={onToggle}>
      <ModalHeader toggle={onToggle}>
        {i18n.t("items.delete.title")}
      </ModalHeader>
      <ModalBody>
        <div className="my-2">
          {type === ITEM_TYPES.LANGUAGE
            ? i18n.t("items.delete.languagesMessage", {name: item.name})
            : i18n.t("items.delete.tagsMessage", {name: item.name})}
        </div>
      </ModalBody>
      <ModalFooter>
        <Button color="danger" onClick={onToggle}>
          {i18n.t("items.delete.cancel")}
        </Button>
        <Button
          className="form-button"
          onClick={() => handleDeleteConfirm(item.id)}
        >
          {i18n.t("items.delete.confirm")}
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default DeleteItemModal;
