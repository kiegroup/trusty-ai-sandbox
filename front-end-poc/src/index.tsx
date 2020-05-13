import React from "react";
import ReactDOM from "react-dom";
import "@patternfly/react-core/dist/styles/base.css";
import "./index.css";
import App from "./App";
import { HashRouter } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

ReactDOM.render(
  <HashRouter>
    <App />
    <ToastContainer closeButton={<></>} className="toast-notification-container" />
  </HashRouter>,
  document.getElementById("root")
);
