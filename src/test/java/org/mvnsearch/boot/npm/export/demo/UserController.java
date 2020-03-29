package org.mvnsearch.boot.npm.export.demo;

import org.mvnsearch.boot.npm.export.NpmPackage;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @GetMapping("/email/{id}")
    @Deprecated
    public Mono<String> findEmailById(@PathVariable("id") Integer id) {
        return Mono.just("email: " + 1);
    }

}
