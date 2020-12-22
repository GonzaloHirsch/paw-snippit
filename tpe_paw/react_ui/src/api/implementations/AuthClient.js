import Client from "../Client";
import axios from "axios";

export default class AuthClient extends Client {
  login(user, pass) {
    return this.instance.post("auth/login", {
      username: user,
      password: pass,
    });
  }

  signup(user, email, pass, repeatPass) {
    return this.instance.post("users", {
      username: user,
      email: email,
      password: pass,
      repeatPassword: repeatPass,
    });
  }

  sendRecoveryEmail(email) {
    return this.instance.post("users/recover_password", {
      email: email,
    });
  }

  sendVerifyEmail(id) {
    return this.instance.post("users/" + id + "/send_verify_email");
  }

  verifyEmail(id, code) {
    return this.instance.post("users/" + id + "/verify_email", {
      code: code,
    });
  }

  isTokenValid(id, token) {
    const params = new URLSearchParams({ token: token });
    const url = this.buildBaseUrl();
    return axios.get(
      url + "users/" + id + "/valid_token?" + params.toString(),
      {
        timeout: 60000,
      }
    );
  }

  changePassword(id, token, data) {
    // Setting the token as part of the request body
    data.token = token;
    return this.instance.post("users/" + id + "/change_password", data);
  }
}
