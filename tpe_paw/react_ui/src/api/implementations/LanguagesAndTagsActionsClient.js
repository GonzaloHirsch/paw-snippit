import Client from "../Client";
import axios from "axios";

export default class LanguagesAndTagsActionsClient extends Client {
  followTag(id) {
    return this.instance.put("tags/" + id + "/follow");
  }

  unfollowTag(id) {
    return this.instance.delete("tags/" + id + "/follow");
  }
}
