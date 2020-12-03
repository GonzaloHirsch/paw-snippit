import Client from "../Client";

export default class AuthClient extends Client {
  login(user, pass) {
    return this.instance.post("auth/login", {
      username: user,
      password: pass,
    });
  }

  sendRecoveryEmail(email) {
    return this.instance.post("users/recover_password", {
      email: email,
    });
  }

  isTokenValid(id, token) {
    return this.instance.post("users/" + id + "/valid_token", {
      token: token,
    });
  }

  changePassword(id, token, data) {
    // Setting the token as part of the request body
    data.token = token;
    return this.instance.post("users/" + id + "/change_password", data);
  }
}
