import * as actions from "./actionTypes";

export const loginSuccess = (token) => ({
  type: actions.LOGIN_SUCCESS,
  payload: {
    token: token,
  },
});

export const logOut = () => ({
  type: actions.LOGOUT,
  payload: {},
});
