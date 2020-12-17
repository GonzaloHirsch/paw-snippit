import { Badge } from "reactstrap";
import { mdiCloseCircle } from "@mdi/js";
import Icon from "@mdi/react";
import { Link } from "react-router-dom";

const TagBadge = (props) => {
  return (
    <div className="pb-2">
      <Badge
        color="secondary"
        pill
        className="d-flex justify-space-between align-items-center tag-badge p-0"
      >
        <Link
          to={"/tags/" + props.tag.id}
          className="flex-center parent-width parent-height word-break no-decoration"
          style={{ minHeight: "30px", marginTop: "2px", marginBottom: "2px" }}
        >
          {props.tag.name}
        </Link>

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
