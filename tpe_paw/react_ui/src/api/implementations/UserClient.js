import Client from "../Client";
import axios from "axios";

export default class UserClient extends Client {
  getUserWithId(id) {
    return this.instance.get("users/" + id);
  }

  getUserWithUrl(url) {
    return axios.get(url);
  }
}
