npm-export-spring-boot-starter
==============================

A Spring Boot starter, generates npm package for Node.js and Browser to call Spring Boot REST API.

# Features

* @NpmPackage("@UserService/UserController") for Spring Boot Controller
* Generate axios stub to call remote REST API
* JSDoc support for code completion
* index.d.ts generation for TypeScript
* OpenAPI support
* Package list: http://localhost/npm/packages

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

```java
@RestController
@RequestMapping("/user")
@NpmPackage("@UserService/UserController")
public class UserController {

    @GetMapping("/user/schemaRaw/{id}")
    @ApiResponse(content = @Content(schema = @Schema(name = "UserExtra", requiredProperties = {"first: string", "last: string"})))
    public Mono<ByteBuffer> findUserByIdSchemaRaw(@PathVariable("id") Integer id) {
        return Mono.empty();
    }

    @GetMapping("/user/schemaBean/{id}")
    @ApiResponse(content = @Content(schema = @Schema(implementation = User.class)))
    public Mono<ByteBuffer> findUserByIdSchemaBean(@PathVariable("id") Integer id) {
        return Mono.empty();
    }
}
```

# Deno Integration

If you use Deno to run TypeScript, please use following code:

```typescript
import  {userController } from "http://localhost:8080/deno/UserService/mod.ts"

let nick = await userController.findNickById(1)
```

# References

* axios: Promise based HTTP client for the browser and node.js https://github.com/axios/axios
* Apache Commons Compress: http://commons.apache.org/proper/commons-compress/
* JSDoc 3: https://jsdoc.app/
* SpringDoc OpenAPI: https://springdoc.org/
* Fetch API: https://developer.mozilla.org/zh-CN/docs/Web/API/Fetch_API/Using_Fetch
