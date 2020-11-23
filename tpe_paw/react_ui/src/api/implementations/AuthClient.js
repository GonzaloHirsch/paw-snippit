import Client from "../Client";
import querystring from querystring

export default class AuthClient extends Client {
    login (user, pass){
        return this.instance.post(
        'auth/login',
        querystring.stringify({username: user, password: pass})
      );
    }
}
