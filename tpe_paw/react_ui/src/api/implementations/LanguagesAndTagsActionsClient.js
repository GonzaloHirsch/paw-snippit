import Client from "../Client";

export default class LanguagesAndTagsActionsClient extends Client {
  // TAGS
  // ------------------------------------------------------------

  followTag(id) {
    return this.instance.put("tags/" + id + "/follow");
  }

  unfollowTag(id) {
    return this.instance.delete("tags/" + id + "/follow");
  }

  deleteTag(id){
    return this.instance.delete("tags/" + id);
  }

  // LANGUAGES
  // ------------------------------------------------------------

  deleteLanguage(id){
    return this.instance.delete("languages/" + id);
  }
}
