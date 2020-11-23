import * as actions from "./actionTypes";

export const loginSuccess = (token) => ({
  type: actions.LOGIN_SUCCESS,
  payload: {
    token: token,
  },
});
