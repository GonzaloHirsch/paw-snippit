import * as actions from "../actions/actionTypes";
import jwt_decode from 'jwt-decode'

// Initial state for Redux, it is only important to hold the authentication status
const initialState = {
  auth: {
    status: actions.LOGIN_PENDING,
    token: null,
    refreshToken: null,
    info: null,
    refreshInfo: null,
    roles: [],
    remember: false
  },
};

export default function reducer(state = initialState, action) {
  switch (action.type) {
    case actions.LOGIN_SUCCESS: {
      // Store the payload, which is the token
      const {token} = action.payload.token;
      const {refreshToken} = action.payload.refreshToken;
      // Extract the pure token
      const pureToken = token.replace("Bearer ", "");
      const pureRefreshToken = refreshToken.replace("Bearer ", "");
      // We have to decode the token
      let content = jwt_decode(pureToken);
      let refreshContent = jwt_decode(pureRefreshToken);
      return {
        ...state,
        auth: {
          status: actions.LOGIN_SUCCESS,
          token: token,
          refreshToken: refreshToken,
          info: content,
          refreshInfo: refreshContent,
          roles: content.auth.map(elem => elem.authority),
          remember: action.payload.remember
        },
      };
    }
    case actions.LOGOUT: {
      return {
        ...state,
        auth: {
          status: actions.LOGOUT,
          token: null,
          refreshToken: null,
          info: null,
          refreshInfo: null,
          roles: [],
          remember: false
        },
      };
    }
    case actions.LOGIN_EXPIRED: {
      return {
        ...state,
        auth: {
          status: actions.LOGIN_EXPIRED,
          token: null,
          refreshToken: null,
          info: null,
          refreshInfo: null,
          roles: [],
          remember: false
        },
      };
    }
    default:{
        // Return the current state for default
        return state;
    }
  }
}
