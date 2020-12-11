import React, { Component } from "react";
import { getUserProfilePicUrl } from "../../js/snippet_utils";

class ImagePicker extends Component {
  //   handleFileChange(event) {
  //     const { target } = event;
  //     const { files } = target;

  //     if (files && files[0]) {
  //       var reader = new FileReader();

  //       reader.onloadstart = () => this.setState({ loading: true });

  //       reader.onload = (event) => {
  //         this.setState({
  //           data: event.target.result,
  //           loading: false,
  //         });
  //       };

  //       reader.readAsDataURL(files[0]);
  //     }
  //   }

  _preview(input) {
    console.log(input.target);
    console.log(input.target.value);
    console.log(input.target.files);
    let output = document.getElementById("profile-image");
    output.src = URL.createObjectURL(input.target.files[0]);
    output.onload = function () {
      URL.revokeObjectURL(output.src);
    };
    //   let save_btn = document.getElementById("image-confirm");
    //   let discard_btn = document.getElementById("image-discard");
    //   save_btn.classList.add("visible");
    //   discard_btn.classList.add("visible");
    //   save_btn.classList.remove("hidden-button");
    //   discard_btn.classList.remove("hidden-button");
  }

  _hiddenClick() {
    let input = document.getElementById("profile-image-input");
    input.click();
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
              src={this.props.imageScr}
              alt="User Icon"
              onClick={() => this._hiddenClick()}
            ></img>
          </div>
        </div>
        <div className="flex-row hidden-code">
          <input
            type="file"
            id="profile-image-input"
            className="hidden-code"
            name="file"
            accept="image/jpeg"
            onChange={(e) => this._preview(e)}
          />
          {/* <input type="submit" id="image-form-submit" /> */}
        </div>
      </React.Fragment>
    );
  }
}

export default ImagePicker;
