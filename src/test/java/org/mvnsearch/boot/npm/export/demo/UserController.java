package org.mvnsearch.boot.npm.export.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.mvnsearch.boot.npm.export.NpmPackage;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * User controller
 *
 * @author linux_china
 */
@RestController
@RequestMapping("/user")
@NpmPackage(name = "@xxx/UserController")
@CrossOrigin("*")
public class UserController {

    @GetMapping("/nick/{id}")
    @Operation(description = "find nick by id")
    public Mono<String> findNickById(@PathVariable("id") Integer id) {
        return Mono.just("nick: " + 1);
    }

    @GetMapping("/nick/2/{id}")
    public String findNickById2(@PathVariable("id") Integer id) {
        return "nick: " + 1;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        return new User(id, "nick", new Date());
    }

    @PostMapping("/save")
    public Integer save(@RequestBody User user) {
        return 1;
    }

    @GetMapping("/email/{id}")
    public Mono<String> findEmailByIdAndNick(@PathVariable("id") Integer id,
                                             @RequestParam(required = false) String nick) {
        return Mono.just("email: " + 1);
    }

    @GetMapping("/email2/{id}")
    @Deprecated
    public Mono<String> findEmailById(@PathVariable("id") Integer id) {
        return Mono.just("email: " + 1);
    }

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

}
