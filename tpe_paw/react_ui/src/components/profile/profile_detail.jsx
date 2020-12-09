import React, { Component } from "react";
import ProfileVerifiedMessage from "./profile_verify_message";

class Profile extends Component {
  state = {};
  render() {
    return (
      <React.Fragment>
        <ul class="nav nav-tabs">
          <li class="nav-item">
            <a class="nav-link active" href="#">
              Active
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              Link
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              Link
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled" href="#">
              Disabled
            </a>
          </li>
        </ul>
      </React.Fragment>
    );
  }
}

export default Profile;
