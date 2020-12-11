import * as actions from "./actionTypes";

export const loginSuccess = (token, refreshToken, remember) => ({
  type: actions.LOGIN_SUCCESS,
  payload: {
    token: token,
    refreshToken: refreshToken,
    remember: remember
  },
});

export const logOut = () => ({
  type: actions.LOGOUT,
  payload: {},
});

export const loginExpired = () => ({
  type: actions.LOGIN_EXPIRED,
  payload: {},
});
