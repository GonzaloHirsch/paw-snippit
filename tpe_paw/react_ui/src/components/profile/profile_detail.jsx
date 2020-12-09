import React from "react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import ProfileStatItem from "./profile_stat_item";
import i18n from "../../i18n";
import ProfileVerifiedMessage from "./profile_verify_message";

const ProfileDetail = (props) => {
  const { owner, loading } = props;
  if (loading) {
    return <div>{i18n.t("loading.general")}</div>;
  } else {
    return (
      <div className="m-3 d-flex flex-row">
        <img
          className="profile-photo"
          src={getUserProfilePicUrl(owner)}
          alt="User Icon"
        />
        <div className="d-flex flex-col justify-content-center">
          <h1 className="no-margin">{owner.username}</h1>
          <span className="fw-300 profile-small-text">Date Joined TODO</span>
          <div className="d-flex my-2">
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
          <p className="profile-small-text">{owner.description}</p>
        </div>
      </div>
    );
  }
};

export default ProfileDetail;
