import * as actions from "../actions/actionTypes";

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
      const { token } = action.payload;
      // We have to decode the token
      // TODO: DECODE TOKEN
      // Store the updated store
      // TODO: UPDATE PARAMS
      return {
        ...state,
        auth: {
          status: actions.LOGIN_SUCCESS,
          token: null,
          info: null,
          roles: [],
        },
      };
    }
    default:{
        // Return the current state for default
        return state;
    }
  }
}
