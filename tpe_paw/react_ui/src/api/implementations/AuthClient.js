import Client from "../Client";

export default class AuthClient extends Client {
  login(user, pass) {
    return this.instance.post("auth/login", {
      username: user,
      password: pass,
    });
  }
}
