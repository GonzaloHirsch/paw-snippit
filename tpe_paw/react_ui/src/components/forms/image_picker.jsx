import React, { Component } from "react";
import { mdiCheckBold, mdiCloseThick } from "@mdi/js";
import Icon from "@mdi/react";

class ImagePicker extends Component {
  constructor(props) {
    super(props);
    this.state = { image: null, edit: false };
  }

  _preview(input, edit) {
    let output = document.getElementById("profile-image");
    output.src = edit
      ? URL.createObjectURL(input.target.files[0])
      : (output.src = this.props.imageSrc);

    output.onload = function () {
      URL.revokeObjectURL(output.src);
    };
  }

  _hiddenClick() {
    let input = document.getElementById("profile-image-input");
    input.click();
  }

  _submitImageForm() {
    let frm = document.getElementById("image-form-submit");
    frm.click();
  }

  _onFileChange(event) {
    const { files } = event.target;
    this.setState({ image: files[0], edit: true });

    this._preview(event, true);
  }

  _onClickClear() {
    this.setState({
      image: null,
      edit: false,
    });
    this._preview(null, false);
  }

  _onSubmit() {
    this.setState({ edit: false });
    this.props.handleSubmit(this.state.image);
  }

  render() {
    return (
      <React.Fragment>
        <div className="flex-center profile-photo-padding">
          <div className="flex-center profile-photo-wrap">
            <span
              className="material-icons profile-photo-edit-icon"
              onClick={() => this._hiddenClick()}
            >
              create
            </span>

            <img
              id="profile-image"
              className="profile-photo shadow"
              src={this.props.imageSrc}
              alt="User Icon"
              onClick={() => this._hiddenClick()}
            ></img>
          </div>
        </div>
        <form
          enctype="multipart/form-data"
          onSubmit={() => this._onSubmit()}
          className="flex-row"
        >
          <div
            id="image-discard"
            className={
              "image-discard-button image-action-container " +
              (this.state.edit ? "visible" : "hidden-button")
            }
            onClick={() => this._onClickClear()}
          >
            <Icon
              className="image-action-icon"
              path={mdiCloseThick}
              size={1}
            ></Icon>
          </div>
          <div
            id="image-confirm"
            className={
              "image-confirm-button image-action-container " +
              (this.state.edit ? "visible" : "hidden-button")
            }
            onClick={() => this._submitImageForm()}
          >
            <Icon
              className="image-action-icon"
              path={mdiCheckBold}
              size={1}
            ></Icon>
          </div>

          <input
            type="file"
            id="profile-image-input"
            className="hidden-code"
            name="file"
            accept="image/jpeg"
            onChange={(e) => this._onFileChange(e)}
          />
          <input type="submit" id="image-form-submit" className="hidden-code" />
        </form>
      </React.Fragment>
    );
  }
}

export default ImagePicker;
