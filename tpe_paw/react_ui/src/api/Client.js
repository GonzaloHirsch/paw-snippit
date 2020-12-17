import conf from "../conf";
import axios from "axios";
import {
  logOut,
  loginExpired,
  loginSuccess,
} from "../redux/actions/actionCreators";
import store from "../store";

// Client parent class for all children to have axios instance
// In order to use the clients, we have to create an instance of the client and call the method
// We can choose to await it or not
class Client {
  protocol;
  domain;
  port;
  base;
  instance;

  constructor(props, recvToken) {
    let token;

    // In case there are props, add the token
    if (recvToken) {
      token = recvToken;
    }

    this.protocol = conf.API_PROTOCOL;
    this.domain = conf.API_DOMAIN;
    this.port = conf.API_PORT;
    this.base = conf.API_BASE;

    // Define the headers, if no token is present, it will be ignored or error
    const headers = {
      Authorization: token,
    };

    // Getting axios instance
    this.instance = axios.create({
      baseURL: `${this.protocol}${this.domain}${this.port}${this.base}`,
      timeout: 60000,
      headers: headers,
    });

    // Adding interceptors for error pages and token refresh
    this.instance.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        let errorResponse = error.response;
        if (errorResponse.status === 401) {
          const refreshInfo = store.getState().auth.refreshInfo;
          const remember = store.getState().auth.remember;
          // Token expired
          if ("TokenError" in errorResponse.data && refreshInfo !== null) {
            const now = Date.now();
            const ts = refreshInfo.exp * 1000;
            // Expired refresh token
            if (ts < now) {
              store.dispatch(logOut());
              props.history.push("/login");
            } else {
              // Getting the token and setting up headers
              const tk = store.getState().auth.refreshToken;
              let config = {
                headers: {
                  Authorization: tk,
                },
              };
              // Dispatching the expired login state
              store.dispatch(loginExpired());
              this.instance
                .post("auth/refresh", {}, config)
                .then((res) => {
                  // Get the token from the response
                  const token = res.data.token;
                  const refreshToken = res.data.refreshToken;

                  // Dispatch the login event
                  store.dispatch(
                    loginSuccess({ token }, { refreshToken }, remember)
                  );

                  // Refresh the page to make the new token changes appear
                  window.location.reload();
                })
                .catch((e) => {});
            }
          } else {
            store.dispatch(logOut());
            props.history.push("/login");
          }
        }
        if (errorResponse.status === 403) {
          store.dispatch(logOut());
          props.history.push("/login");
        }
        if (errorResponse.status === 500) {
          props.history.push("/500");
        }
        return Promise.reject(error);
      }
    );
  }

  getWithUrl(url) {
    return axios.get(url);
  }
}

export default Client;
