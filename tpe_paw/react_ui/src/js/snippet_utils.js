export function getUserProfilePicUrl(creator) {
  if (creator.hasPicture) {
    return creator.picture;
  }
  return "/userIcon.jpg";
}

export function getSnippetPositionInArray(snippets, id){
  for (let i in snippets){
    if (snippets[i].id === id){
      return i;
    }
  }
  return -1;
}