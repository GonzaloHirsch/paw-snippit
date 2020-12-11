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

  putUserImage(id, image) {
    return this.instance.put("users/" + id + "/profile_photo", {
      file: image,
    });
  }

  getUserWithUrl(url) {
    return axios.get(url);
  }
}
