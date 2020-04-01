npm-export-spring-boot-starter
==============================

A Spring Boot starter, generates npm package for Node.js and Browser to call Spring Boot REST API.

# Features

* Generate axios stub to call remote REST API
* JSDoc support for code completion
* OpenAPI support

# How to use?

* Include dependency in your package.json and run "yarn install"

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

### OpenAPI Integration

* @Operation
* @ApiResponse

```
    @GetMapping("/user/schemaRaw/{id}")
    @ApiResponse(content = @Content(schema = @Schema(name = "UserExtra", requiredProperties = {"{boolean} first", "{boolean} second"})))
    public Mono<ByteBuffer> findUserByIdSchemaRaw(@PathVariable("id") Integer id) {
        return Mono.empty();
    }

    @GetMapping("/user/schemaBean/{id}")
    @ApiResponse(content = @Content(schema = @Schema(implementation = User.class)))
    public Mono<ByteBuffer> findUserByIdSchemaBean(@PathVariable("id") Integer id) {
        return Mono.empty();
    }
```

# References

* axios: Promise based HTTP client for the browser and node.js https://github.com/axios/axios
* Apache Commons Compress: http://commons.apache.org/proper/commons-compress/
* JSDoc 3: https://jsdoc.app/
* SpringDoc OpenAPI: https://springdoc.org/
