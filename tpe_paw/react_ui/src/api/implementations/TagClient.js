import Client from "../Client";
import axios from "axios";

export default class TagClient extends Client {
  getTagWithId(id) {
    return this.instance.get("tags/" + id);
  }

  getTagList() {
    return this.instance.get("tags/all");
  }

  getTagWithUrl(url) {
    return axios.get(url);
  }

  getTagListWithUrl(url){
    return axios.get(url);
  }
}
