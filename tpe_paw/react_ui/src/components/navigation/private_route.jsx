import React, { Component } from "react";
import { Redirect, Route } from "react-router-dom";
import store from "../../store";

// Function to determine if a user's current roles are enough to render the page
function checkIfRolesAreOk(okRoles, currentRoles) {
  var isOk = false;
  for (var i = 0; i < currentRoles.length; i++) {
    for (var j = 0; i < okRoles.length; j++) {
      if (currentRoles[i] === okRoles[j]) {
        isOk = true;
        break;
      }
    }
  }
  return isOk;
}

export const PrivateRoute = ({...rest }) => {
  // Roles that can access the path
  const okRoles = rest.roles;
  // Current user roles
  const currentRoles = store.getState().auth.roles;
  // If the roles are ok, the component is rendered, if not, a redirect to login is made
  return (
    <Route
      {...rest}
      render={({ location }) =>
        checkIfRolesAreOk(okRoles, currentRoles) ? (
          <rest.component />
        ) : (
          <Redirect
            to={{
              pathname: currentRoles.length > 0 ? "/" : "/login",
              state: { from: location },
            }}
          />
        )
      }
    />
  );
};
