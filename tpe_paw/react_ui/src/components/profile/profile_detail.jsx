import React, { Component } from "react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import ProfileStatItem from "./profile_stat_item";
import i18n from "../../i18n";
import { mdiPencil } from "@mdi/js";
import Icon from "@mdi/react";
import ProfileVerifiedMessage from "./profile_verify_message";

class ProfileDetail extends Component {
  render() {
    const { owner, loading, loggedUser } = this.props;
    if (loading) {
      return <div>{i18n.t("loading.general")}</div>;
    } else {
      return (
        <React.Fragment>
          <img
            className="profile-photo shadow"
            src={getUserProfilePicUrl(owner)}
            alt="User Icon"
          />
          <div className="d-flex flex-col justify-content-center parent-width fwhite">
            <div className="fw-500  profile-username">{owner.username}</div>
            <span className="fw-100 profile-small-text d-flex justify-content-center">
              Date Joined TODO
            </span>
            <div className="flex-center flex-wrap my-2">
              <ProfileStatItem
                itemName="Snippets"
                itemCount={owner.stats.activeSnippetCount}
              />
              <ProfileStatItem
                itemName="Reputation"
                itemCount={owner.stats.reputation}
              />
              <ProfileStatItem
                itemName="Following"
                itemCount={owner.stats.followingCount}
              />
            </div>
            <div className="my-3 profile-small-text align-items-horizontal-center">
              {owner.description}
            </div>
          </div>
          <button
            className={
              "btn btn-lg btn-primary btn-block mt-2 mb-1 rounded-border ld-over-inverse profile-edit-button " +
              (false ? "running" : "")
            }
          >
            <div className="ld ld-ring ld-spin"></div>
            <Icon
              className="profile-edit-icon"
              path={mdiPencil}
              size={1}
            ></Icon>

            {i18n.t("profile.editButton")}
          </button>
        </React.Fragment>
      );
    }
  }
}

export default ProfileDetail;
