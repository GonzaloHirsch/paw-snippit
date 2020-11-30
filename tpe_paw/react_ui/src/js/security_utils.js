// Function to determine if the given user is a USER or not
export function isUser(roles) {
  for (var i = 0; i < roles.length; i++) {
    if (roles[i] === ROLE_USER) {
      return true;
    }
  }
  return false;
}

// Function to determine if the given user is an ADMIN or not
export function isAdmin(roles) {
  for (var i = 0; i < roles.length; i++) {
    if (roles[i] === ROLE_ADMIN) {
      return true;
    }
  }
  return false;
}

export const ROLE_USER = "USER";
export const ROLE_ADMIN = "ADMIN";
