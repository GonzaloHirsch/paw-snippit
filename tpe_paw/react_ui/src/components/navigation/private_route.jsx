import React from "react";
import { Redirect, Route } from "react-router-dom";
import store from "../../store";

// Function to determine if a user's current roles are enough to render the page
function checkIfRolesAreOk(okRoles, currentRoles) {
  var isOk = false;
  for (var i = 0; i < currentRoles.length; i++) {
    for (var j = 0; j < okRoles.length; j++) {
      if (currentRoles[i] === okRoles[j]) {
        isOk = true;
        break;
      }
    }
  }
  return isOk;
}

// https://stackoverflow.com/questions/6229197/how-to-know-if-two-arrays-have-the-same-values
// Function to check if both arrays contain exactly the same elements
function checkIfStrictRolesAreOk(okRoles, currentRoles) {
  if (okRoles.length !== currentRoles.length) {
    return false;
  }

  const arr1 = okRoles.concat().sort();
  const arr2 = currentRoles.concat().sort();

  for (let i = 0; i < arr1.length; i++) {
    if (arr1[i] !== arr2[i]) {
      return false;
    }
  }
  return true;
}

function checkIfAnonIsOk(isAnon, currentRoles) {
  return !(isAnon && currentRoles.length > 0);
}

// This functional component wraps a Route component in order to provide the functionality to redirect a user if it does not have permissions
export const PrivateRoute = ({ ...rest }) => {
  // Check if the route is anonymous
  const isAnon = rest.anon;
  // Check if the route is strict
  const isStrict = rest.strict;
  // Roles that can access the path
  const okRoles = rest.roles;
  // Current user roles
  const currentRoles = store.getState().auth.roles;
  console.log(isAnon, "ANO");
  console.log(checkIfAnonIsOk(isAnon, currentRoles), "NONON");
  console.log(checkIfRolesAreOk(okRoles, currentRoles), "ROLES");
  // If the roles are ok, the component is rendered, if not, a redirect to login is made
  return (
    <Route
      {...rest}
      render={({ location }) =>
        isStrict ? (
          checkIfStrictRolesAreOk(okRoles, currentRoles) ? (
            <rest.comp />
          ) : (
            <Redirect
              to={{
                pathname: "/",
                state: { from: location },
              }}
            />
          )
        ) : isAnon ? (
          checkIfAnonIsOk(isAnon, currentRoles) ? (
            <rest.comp />
          ) : (
            <Redirect
              to={{
                pathname: "/",
                state: { from: location },
              }}
            />
          )
        ) : checkIfRolesAreOk(okRoles, currentRoles) ? (
          <rest.comp />
        ) : (
          <Redirect
            to={{
              pathname: currentRoles.length > 0 || isAnon ? "/" : "/login",
              state: { from: location },
            }}
          />
        )
      }
    />
  );
};
