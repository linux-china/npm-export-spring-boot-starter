npm-export-spring-boot-starter
==============================

A Spring Boot starter, generates npm package for Node.js and Browser to call Spring Boot REST API.

# How to use?

* Include dependency in your package.json

```
 "dependencies": {
    "@UserService/UserController": "http://localhost:8080/npm/@UserService/UserController"
  }
```

* Call service api in your JS code: 

```
const userController = require("@UserService/UserController").setBaseUrl("http://localhost:8080");

(async () => {
    let nick = await userController.findNickById(1);
    console.log(nick);
})()
```


# References

* axios: Promise based HTTP client for the browser and node.js https://github.com/axios/axios
* Apache Commons Compress: http://commons.apache.org/proper/commons-compress/
