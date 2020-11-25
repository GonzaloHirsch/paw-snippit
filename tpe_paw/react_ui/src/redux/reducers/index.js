import * as actions from "../actions/actionTypes";
import jwt_decode from 'jwt-decode'

// Initial state for Redux, it is only important to hold the authentication status
const initialState = {
  auth: {
    status: actions.LOGIN_PENDING,
    token: null,
    info: null,
    roles: [],
  },
};

export default function reducer(state, action) {
  switch (action.type) {
    case actions.LOGIN_SUCCESS: {
      // Store the payload, which is the token
      const {token} = action.payload.token;
      // Extract the pure token
      const pureToken = token.replace("Bearer ", "");
      // We have to decode the token
      let content = jwt_decode(pureToken);
      // Store the updated store
      return {
        ...state,
        auth: {
          status: actions.LOGIN_SUCCESS,
          token: token,
          info: content,
          roles: [],  // TODO: CHECK ROLES
        },
      };
    }
    default:{
        // Return the current state for default
        return state;
    }
  }
}
