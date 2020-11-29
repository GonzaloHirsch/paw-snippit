import Client from "../Client";
import axios from "axios";

export default class LanguageClient extends Client {
  getLanguageWithId(id) {
    return this.instance.get("languages/" + id);
  }

  getLanguageWithUrl(url) {
    return axios.get(url);
  }
}
