export function getUserProfilePicUrl(creator) {
  console.log(process.env.PUBLIC_URL, "URL")
  if (creator.hasPicture) {
    return creator.picture;
  }
  return process.env.PUBLIC_URL + "/userIcon.jpg";
}

export function getSnippetPositionInArray(snippets, id) {
  for (let i in snippets) {
    if (snippets[i].id === id) {
      return i;
    }
  }
  return -1;
}
