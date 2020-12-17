import { mdiDelete } from "@mdi/js";
import Icon from "@mdi/react";

const ItemListElement = (props) => {
  const { index, item, onDelete } = props;
  return (
    <div
      className={
        "px-4 py-2 item-element-container d-flex justify-space-between " +
        (item.exists && "item-element-exists")
      }
    >
      <div>
        <span className="mr-3 font-weight-bold">{index}.</span>
        <span>{item.name}</span>
      </div>
      <Icon
        className="icon-delete"
        path={mdiDelete}
        size={1}
        onClick={() => onDelete(props.index - 1)}
      />
    </div>
  );
};

export default ItemListElement;
