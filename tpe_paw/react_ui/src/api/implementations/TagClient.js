import Client from "../Client";
import axios from "axios";

export default class TagClient extends Client {
  getTagWithId(id) {
    return this.instance.get("tags/" + id);
  }

  getTags() {
    return this.instance.get("tags");
  }

  getTagWithUrl(url) {
    return axios.get(url);
  }
}
