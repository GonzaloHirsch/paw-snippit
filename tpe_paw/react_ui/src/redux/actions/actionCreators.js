import * as actions from "./actionTypes";

export const loginSuccess = (token, remember) => ({
  type: actions.LOGIN_SUCCESS,
  payload: {
    token: token,
    remember: remember
  },
});

export const logOut = () => ({
  type: actions.LOGOUT,
  payload: {},
});
