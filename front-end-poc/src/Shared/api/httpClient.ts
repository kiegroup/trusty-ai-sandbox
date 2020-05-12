import axios, { AxiosRequestConfig, CancelTokenSource } from "axios";

const httpClient = axios.create({
  baseURL: "http://localhost:1336",
  timeout: 5000,
  headers: {},
});

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

export { httpClient, callOnce };
