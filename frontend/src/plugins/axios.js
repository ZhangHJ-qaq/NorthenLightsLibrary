"use strict";

import Vue from 'vue';
import axios from "axios";

// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

let config = {
  // baseURL: process.env.baseURL || process.env.apiUrl || ""
  // timeout: 60 * 1000, // Timeout
  // withCredentials: true, // Check cross-site Access-Control
  baseURL: 'http://localhost:8080/api'
};

const _axios = axios.create(config);

_axios.interceptors.request.use(
  function(config) {
    // Do something before request is sent
    if(store.state.token) {
      // 判断是否有token，若存在，每个http header加上token
      config.headers.Authorization = 'Bearer ${store.state.token}';
    }
    return config;
  },
  function(error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
_axios.interceptors.response.use(
  function(response) {
    // Do something with response data
    return response;
  },
  function(error) {
    // Do something with response error
    return Promise.reject(error);
  }
);

export function request(config) {
  const instance = axios.create({
    baseURL: 'http://localhost:8888',
    timeout: 5000,
    'Content-Type' : 'multipart/form-data',
    withCredentials:true
  })
  instance.interceptors.request.use(
      function(config) {
        // Do something before request is sent
        if(store.state.token) {
          // 判断是否有token，若存在，每个http header加上token
          config.headers.Authorization = 'Bearer ${store.state.token}';
        }
        return config;
      },
      function(error) {
        // Do something with request error
        return Promise.reject(error);
      }
  );
  return instance(config)

}

Plugin.install = function(Vue, options) {
  Vue.axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    axios: {
      get() {
        return _axios;
      }
    },
    $axios: {
      get() {
        return _axios;
      }
    },
  });
};

Vue.use(Plugin)

export default Plugin;
