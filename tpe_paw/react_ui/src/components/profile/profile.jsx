import React, { Component } from "react";

class Profile extends Component {
  state = {};
  render() {
    return (
      <ProfileVerifiedMessage id={this.state.info.id}></ProfileVerifiedMessage>
    );
  }
}

export default Profile;
