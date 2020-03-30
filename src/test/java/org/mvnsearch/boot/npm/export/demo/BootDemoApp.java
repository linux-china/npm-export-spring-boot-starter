package org.mvnsearch.boot.npm.export.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * boot demo app
 *
 * @author linux_china
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "UserService App", description = "User related REST API"))
public class BootDemoApp {
    public static void main(String[] args) {
        SpringApplication.run(BootDemoApp.class, args);
    }
}
