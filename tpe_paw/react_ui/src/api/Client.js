import conf from "../conf";
import axios from "axios";

// Client parent class for all children to have axios instance
// In order to use the clients, we have to create an instance of the client and call the method
// We can choose to await it or not
class Client {
  protocol;
  domain;
  port;
  base;
  instance;

  constructor(props) {
    let token;

    // In case there are props, add the token
    if (props) {
      token = props.token;
    }

    this.protocol = conf.API_PROTOCOL;
    this.domain = conf.API_DOMAIN;
    this.port = conf.API_PORT;
    this.base = conf.API_BASE;

    // Define the headers, if no token is present, it will be ignored or error
    const headers = {
      "Authorization": token,
    };

    // Getting axios instance
    this.instance = axios.create({
      baseURL: `${this.protocol}${this.domain}${this.port}${this.base}`,
      timeout: 60000,
      headers: headers,
    });

    // TODO: ADD INTERCEPTORS FOR ERRORS
  }
}

export const TOKEN_HEADER = "Authorization";
export default Client;
