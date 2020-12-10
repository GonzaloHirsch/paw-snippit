import Client from "../Client";
import axios from "axios";

export default class UserClient extends Client {
  getUserWithId(id) {
    return this.instance.get("users/" + id);
  }

  putUserDescription(id, description) {
    return this.instance.put("users/" + id + "/description", {
      description: description,
    });
  }

  getUserWithUrl(url) {
    return axios.get(url);
  }
}
