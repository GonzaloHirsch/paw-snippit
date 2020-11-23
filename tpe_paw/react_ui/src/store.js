import { createStore } from "redux";
import reducer from "./redux/reducers";

// Creating the store using our reducer
const store = createStore(reducer);

export default store;