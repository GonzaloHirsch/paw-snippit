import Client from "../Client";
import axios from "axios";

export default class LanguageClient extends Client {
  getTagWithId(id) {
    return this.instance.get("tags/" + id);
  }

  getTagWithUrl(url) {
    return axios.get(url);
  }
}
