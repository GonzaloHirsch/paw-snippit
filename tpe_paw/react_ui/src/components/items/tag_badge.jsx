import { Badge } from "reactstrap";
import { mdiCloseCircle } from "@mdi/js";
import Icon from "@mdi/react";

const TagBadge = (props) => {
  return (
    <div className="pb-1">
      <Badge
        color="secondary"
        pill
        className="d-flex justify-space-between align-items-center tag-badge word-break pl-3 pr-0"
      >
        {props.tag.name}
        <Icon
          className="tag-badge-icon"
          onClick={() => props.onUnfollow(props.tag)}
          path={mdiCloseCircle}
          size={1}
        />
      </Badge>
    </div>
  );
};

export default TagBadge;
