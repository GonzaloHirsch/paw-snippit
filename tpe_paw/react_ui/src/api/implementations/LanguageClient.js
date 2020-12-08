import Client from "../Client";
import axios from "axios";

export default class LanguageClient extends Client {
  getLanguageWithId(id) {
    return this.instance.get("languages/" + id);
  }

  getLanguageList() {
    return this.instance.get("languages/all");
  }

  getLanguageWithUrl(url) {
    return axios.get(url);
  }
}
