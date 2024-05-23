package com.management_system.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {
                "com.management_system.authentication.usecases",
                "com.management_system.authentication.entities",
                "com.management_system.authentication.infrastructure",
                "com.management_system.authentication",
                "com.management_system.utilities",
        }
)
@ComponentScan(basePackages = {
        "com.management_system.authentication.usecases",
        "com.management_system.authentication.entities",
        "com.management_system.authentication.infrastructure",
        "com.management_system.authentication",
        "com.management_system.utilities",
})
public class AuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}
