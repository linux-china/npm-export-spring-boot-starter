// Don't edit this file because it was generated by Spring Boot App!!!
const axios = require('axios');

let isBrowser = new Function('try {return this===window;}catch(e){ return false;}');
let isNode = new Function('try {return this===global;}catch(e){return false;}');

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

/**
 * @param uri {string}
 * @param pathVariables {object}
 * @return {string}
 */
function formatUri(uri, pathVariables) {
    let newUri = uri;
    for (const name in pathVariables) {
        newUri = newUri.replace('*{' + name + '}', pathVariables[name])
                .replace('{' + name + '}', pathVariables[name])
    }
    return newUri;
}

/**
 * @version 2020.03.30
 */
class UserController {
    constructor() {
        if (isBrowser()) {
            this.baseUrl = '';
        } else {
            this.baseUrl = 'http://127.0.0.1:8080'
        }
    }

    /**
     * set base url
     * @param baseUrl base url
     * @returns {UserController}
     */
    setBaseUrl(baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * set JWT Token
     * @param token token token
     * @return {UserController}
     */
    setJwtToken(token) {
        this.jwtToken = token;
        return this;
    }

    /**
     * set config filter
     * @param filter {function}
     * @return {UserController}
     */
    setConfigFilter(filter) {
        this.configFilter = filter;
        return this;
    }

    /**
    *
    * @param {org_mvnsearch_boot_npm_export_demo_User} user
    * @return {Promise<number>}
    */
    save(user) {
      let config = {
        url: this.baseUrl + '/user/save',
        headers: {"Content-Type": "application/json"},
         data: user,
        method: 'post'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return parseInt(response.data);});
    }

    /**
    * find nick by id
    * @param {number} id
    * @return {Promise<string>}
    */
    findNickById(id) {
      let config = {
        url: this.baseUrl + formatUri('/user/nick/{id}',{"id": id}),
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @param {FeedPost} content
    * @return {Promise<number>}
    */
    postFeed(content) {
      let config = {
        url: this.baseUrl + '/user/user/feed/post',
        headers: {"Content-Type": "application/octet-stream"},
         data: content,
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @return {Promise<string>}
    */
    notFound() {
      let config = {
        url: this.baseUrl + '/user/404',
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @param {number} id
    * @return {Promise<org_mvnsearch_boot_npm_export_demo_User>}
    */
    findUserById(id) {
      let config = {
        url: this.baseUrl + formatUri('/user/{id}',{"id": id}),
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @param {number} id
    * @param {string} [nick]
    * @return {Promise<string>}
    */
    findEmailByIdAndNick(id, nick) {
      let config = {
        url: this.baseUrl + formatUri('/user/email/{id}',{"id": id}),
        headers: {},
        params: {"nick": nick},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @deprecated
    * @param {number} id
    * @return {Promise<string>}
    */
    findEmailById(id) {
      let config = {
        url: this.baseUrl + formatUri('/user/email2/{id}',{"id": id}),
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @param {number} id
    * @return {Promise<UserExtra>}
    */
    findUserByIdSchemaRaw(id) {
      let config = {
        url: this.baseUrl + formatUri('/user/user/schemaRaw/{id}',{"id": id}),
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @param {number} id
    * @return {Promise<org_mvnsearch_boot_npm_export_demo_User>}
    */
    findUserByIdSchemaBean(id) {
      let config = {
        url: this.baseUrl + formatUri('/user/user/schemaBean/{id}',{"id": id}),
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {return response.data;});
    }

    /**
    *
    * @param {number} id
    * @return {Promise<(string|null)>}
    */
    findNickById2(id) {
      let config = {
        url: this.baseUrl + formatUri('/user/nick/2/{id}',{"id": id}),
        headers: {},
        method: 'get'
      };
      if (this.jwtToken != null) { config.headers['Authorization'] = 'Bearer ' + this.jwtToken; }
      if (this.configFilter != null) { config = this.configFilter(config); }
      return axios(config).then(response => {if (response.status === 404) {return null;} return response.data;});
    }

}

module.exports = new UserController();

//================ JSDoc typedef ========================//
/**
* @typedef {Object} org_mvnsearch_boot_npm_export_demo_User
* @property {number} id
* @property {string} nick
* @property {string} birthDate
*/
/**
* @typedef {Object} FeedPost
* @property {string} title
* @property {string} content
*/
/**
* @typedef {Object} UserExtra
* @property {boolean} first
* @property {boolean} second
*/
