export function getUserProfilePicUrl(creator) {
  if (creator.hasPicture) {
    return creator.picture;
  }
  return "/userIcon.jpg";
}
