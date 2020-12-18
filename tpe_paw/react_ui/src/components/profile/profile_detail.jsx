import React, { Component } from "react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";
import ProfileStatItem from "./profile_stat_item";
import i18n from "../../i18n";
import { mdiPencil, mdiContentSaveEdit, mdiTrashCan } from "@mdi/js";
import Icon from "@mdi/react";
import { PROFILE_VALIDATION } from "../../js/validations";
import ImagePicker from "../forms/image_picker";
import { getDateFromAPIString } from "../../js/date_utils";
import { Spinner } from "reactstrap";

class ProfileDetail extends Component {
  constructor(props) {
    super(props);
    this.state = {
      edit: false,
      editCounter: 0,
      description: "",
      errors: {
        description: null,
      },
    };
  }

  // Validate Image or description form
  _validateForm(key) {
    let error = {};
    error[key] = PROFILE_VALIDATION[key](this.state[key]);
    this.setState({ errors: error });
    return error[key] === null;
  }

  // HANDLERS
  _onClickEdit() {
    this.setState({ edit: true });
  }

  _onClickDiscard() {
    const errors = { description: null };
    this.setState({ edit: false });
    this.setState({ errors: errors });
  }

  _onSubmitSaveDescription(e) {
    e.preventDefault();
    // If editCounter == 0, there where no changes and no need to update
    if (this.state.editCounter > 0) {
      if (this._validateForm("description")) {
        this.props.updateDescription(this.state.description);
      } else {
        return; // Do not want to submit or finish editing since there are still errors
      }
    }
    this.setState({ edit: false });
  }

  _onDescriptionChange(e) {
    const counter = this.state.editCounter;
    let errors = this.state.errors;
    errors.description = PROFILE_VALIDATION.description(e);
    this.setState({ description: e, editCounter: counter + 1, errors: errors });
  }

  _renderDiscardButton() {
    return (
      <button
        className={
          "mx-0 mb-2 shadow btn btn-lg btn btn-danger btn-block mt-2 mb-1 rounded-border ld-over-inverse profile-button"
        }
        onClick={() => this._onClickDiscard()}
      >
        <Icon className="profile-edit-icon" path={mdiTrashCan} size={1}></Icon>
        {i18n.t("profile.edit.discard")}
      </button>
    );
  }

  _renderSaveButton() {
    return (
      <button
        className={
          "no-margin shadow btn btn-lg btn-block mt-2 mb-1 rounded-border ld-over-inverse profile-button profile-edit-button " +
          (this.props.descriptionLoading ? "running" : "")
        }
        type="submit"
      >
        <div className="ld ld-ring ld-spin"></div>
        <Icon
          className="profile-edit-icon"
          path={mdiContentSaveEdit}
          size={1}
        ></Icon>
        {i18n.t("profile.edit.save")}
      </button>
    );
  }

  _renderEditButton() {
    return (
      <button
        className={
          "no-margin shadow btn btn-lg btn-block mt-2 mb-1 rounded-border ld-over-inverse profile-button profile-edit-button"
        }
        onClick={() => this._onClickEdit()}
      >
        <Icon className="profile-edit-icon" path={mdiPencil} size={1}></Icon>
        {i18n.t("profile.edit.begin")}{" "}
      </button>
    );
  }

  _renderDescriptionForm() {
    const { owner, loggedUserId } = this.props;
    const error = this.state.errors.description;
    const commonClasses =
      "my-3 profile-small-text align-items-horizontal-center rounded-border parent-width";
    return owner.id === loggedUserId ? (
      this.state.edit || this.props.descriptionLoading ? (
        <form onSubmit={(e) => this._onSubmitSaveDescription(e)}>
          <textarea
            placeholder={i18n.t("profile.form.descriptionPlaceholder")}
            className={
              commonClasses +
              " profile-edit-description " +
              (error && "with-error")
            }
            defaultValue={this.props.owner.description}
            onChange={(e) => this._onDescriptionChange(e.target.value)}
          ></textarea>
          {error && (
            <div className="flex-center word-wrap text-danger parent-width fw-500 profile-edit-description-error">
              {error}
            </div>
          )}

          {this._renderDiscardButton()}
          {this._renderSaveButton()}
        </form>
      ) : (
        <div className="flex-center flex-wrap parent-width">
          <div className={commonClasses}>{this.props.owner.description}</div>
          {this._renderEditButton()}
        </div>
      )
    ) : (
      <div className={commonClasses}>{this.props.owner.description}</div>
    );
  }

  _renderUserIcon() {
    const { owner, loggedUserId } = this.props;
    return owner.id === loggedUserId ? (
      <React.Fragment>
        <ImagePicker
          imageSrc={getUserProfilePicUrl(owner)}
          handleSubmit={this.props.updateImage}
          imageLoading={this.props.imageLoading}
        />
        {this.props.imageRequestErrors && (
          <div className="flex-center word-wrap text-danger parent-width fw-500 profile-edit-description-error">
            {this.props.imageRequestErrors}
          </div>
        )}
      </React.Fragment>
    ) : (
      <div className="flex-center profile-photo-padding">
        <img
          id="profile-image"
          className="profile-photo shadow"
          src={getUserProfilePicUrl(owner)}
          alt="User Icon"
        ></img>
      </div>
    );
  }

  _renderUserDetail() {
    const { owner } = this.props;
    return (
      <div className="flex-center flex-column parent-width">
        {this._renderUserIcon()}

        <div className="d-flex flex-col justify-content-center parent-width fwhite">
          <div className="fw-500  profile-username">{owner.username}</div>
          <span className="fw-100 profile-small-text d-flex justify-content-center">
            {i18n.t("profile.joinedOn", {
              date: getDateFromAPIString(owner.dateJoined),
            })}
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
      </div>
    );
  }

  render() {
    const { loading } = this.props;
    if (loading) {
      return (
        <div className="align-items-vertical align-items-horizontal-center">
          <Spinner color="dark" style={{ width: "3rem", height: "3rem" }} />
        </div>
      );
    } else {
      return <div>{this._renderUserDetail()}</div>;
    }
  }
}

export default ProfileDetail;
