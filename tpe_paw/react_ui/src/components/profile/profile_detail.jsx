import React, { Component } from "react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import ProfileStatItem from "./profile_stat_item";
import i18n from "../../i18n";
import { mdiPencil } from "@mdi/js";
import { mdiContentSaveEdit } from "@mdi/js";
import Icon from "@mdi/react";

class ProfileDetail extends Component {
  constructor(props) {
    super(props);
    this.state = {
      edit: false,
      description: "",
    };
  }

  _validateDescription() {}

  _onEditClick() {
    this.setState({ edit: true });
  }

  _onSubmitSaveDescription() {
    this.setState({ edit: false });
    this._validateDescription();
    this.props.updateDescription(this.state.description);
    console.log("Submir");
  }

  _onDescriptionChange(e) {
    this.setState({ description: e });
  }

  _renderEditButtonText() {
    return this.state.edit ? (
      <div>
        <Icon
          className="profile-edit-icon"
          path={mdiContentSaveEdit}
          size={1}
        ></Icon>
        {i18n.t("profile.edit.save")}
      </div>
    ) : (
      <React.Fragment>
        <Icon className="profile-edit-icon" path={mdiPencil} size={1}></Icon>
        {i18n.t("profile.edit.begin")}
      </React.Fragment>
    );
  }

  _renderDescriptionForm() {
    const { owner, loggedUserId } = this.props;
    const commonClasses =
      "my-3 profile-small-text align-items-horizontal-center rounded-border";
    return owner.id === loggedUserId ? (
      this.state.edit ? (
        <form onSubmit={() => this._onSubmitSaveDescription()}>
          <textarea
            placeholder={i18n.t("profile.edit.descriptionPlaceholder")}
            className={commonClasses + " profile-edit-description "}
            defaultValue={this.props.owner.description}
            onChange={(e) => this._onDescriptionChange(e.target.value)}
          ></textarea>
          <button
            className={
              "no-margin shadow btn btn-lg btn-primary btn-block mt-2 mb-1 rounded-border ld-over-inverse profile-edit-button " +
              (false ? "running" : "")
            }
            type="submit"
          >
            <div className="ld ld-ring ld-spin"></div>
            {this._renderEditButtonText()}
          </button>
        </form>
      ) : (
        <div>
          <div className={commonClasses}>{this.props.owner.description}</div>
          <button
            className={
              "no-margin shadow btn btn-lg btn-primary btn-block mt-2 mb-1 rounded-border ld-over-inverse profile-edit-button " +
              (false ? "running" : "")
            }
            onClick={() => this._onEditClick()}
          >
            <div className="ld ld-ring ld-spin"></div>
            {this._renderEditButtonText()}
          </button>
        </div>
      )
    ) : (
      <div className={commonClasses}>{this.props.owner.description}</div>
    );
  }

  _renderUserDetail() {
    const { owner } = this.props;
    return (
      <React.Fragment>
        {" "}
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

          {this._renderDescriptionForm()}
        </div>
      </React.Fragment>
    );
  }

  render() {
    const { loading } = this.props;
    if (loading) {
      return <div>{i18n.t("loading.general")}</div>;
    } else {
      return <div>{this._renderUserDetail()}</div>;
    }
  }
}

export default ProfileDetail;
