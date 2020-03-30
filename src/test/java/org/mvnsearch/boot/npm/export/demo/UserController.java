package org.mvnsearch.boot.npm.export.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
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
@CrossOrigin("*")
public class UserController {

    @GetMapping("/nick/{id}")
    @Operation(description = "find nick by id")
    public Mono<String> findNickById(@PathVariable("id") Integer id, ServerWebExchange exchange) {
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
    @Schema(name = "UserExtra", requiredProperties = {"{boolean} first", "{boolean} second"})
    public Mono<ByteBuffer> findUserByIdSchemaRaw(@PathVariable("id") Integer id) {
        return Mono.empty();
    }

    @GetMapping("/user/schemaBean/{id}")
    @Schema(implementation = User.class)
    public Mono<ByteBuffer> findUserByIdSchemaBean(@PathVariable("id") Integer id) {
        return Mono.empty();
    }

    @GetMapping("/user/feed/post")
    public Mono<Long> postFeed(@Schema(name = "FeedPost", requiredProperties = {"{string} title", "{string} content"}) @RequestBody byte[] content) {
        return Mono.empty();
    }
}
