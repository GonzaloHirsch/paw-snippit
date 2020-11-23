import React, { Component } from 'react';
import {store} from "../../store"
import {loginSuccess} from "../../redux/actions/actionCreators";

class Login extends Component {
    state = {  }
    render() { 
        // To get the state
        store.getState()

        // To dispatch, the param is the token inside the function
        store.dispatch(loginSuccess({}))
        return ( <h1>HEY LOGIKN</h1> );
    }
}
 
export default Login;