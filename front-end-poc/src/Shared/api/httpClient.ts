import axios, { AxiosRequestConfig, CancelTokenSource } from "axios";
import { toast, Slide } from "react-toastify";
import ErrorNotification from "../components/ErrorNotification/ErrorNotification";

const httpClient = axios.create({
  baseURL: "http://localhost:1336",
  timeout: 5000,
  headers: {},
});

httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      const { status, statusText, data } = error.response;
      if (status === 500) {
        toast(({ closeToast }) => ErrorNotification({ closeToast, message: statusText, details: data.details }), {
          transition: Slide,
          autoClose: 5000,
          hideProgressBar: true,
        });
      }
    } else {
      return Promise.reject(error);
    }
  }
);

/**
 * first implementation of a repeated call canceler
 * basically it manipulates the conf object adding canceler
 * and calling it when repeated calls happens.
 * at some point it will need to check for an identifier
 * if parallel requests need canceling
 * */
let call: CancelTokenSource;
const callOnce = (config: AxiosRequestConfig) => {
  if (call) {
    call.cancel("Request superseded");
  }
  call = axios.CancelToken.source();

  config.cancelToken = call.token;
  return httpClient(config);
};

const isCancelledRequest = axios.isCancel;

export { httpClient, callOnce, isCancelledRequest };
