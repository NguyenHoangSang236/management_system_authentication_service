package com.management_system.authentication;

import com.management_system.utilities.utils.InitiateConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class},
        scanBasePackages = {
                "com.management_system.authentication.usecases",
                "com.management_system.authentication.entities",
                "com.management_system.authentication.infrastructure",
                "com.management_system.authentication.config",
                "com.management_system.authentication",
                "com.management_system.utilities",
        }
)
@ComponentScan(basePackages = {
        "com.management_system.authentication.usecases",
        "com.management_system.authentication.entities",
        "com.management_system.authentication.infrastructure",
        "com.management_system.authentication.config",
        "com.management_system.authentication",
        "com.management_system.utilities",
})
//@EnableDiscoveryClient
public class AuthenticationApplication {
    @Autowired
    InitiateConfigUtils initiateConfigUtils;


    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initSslConfig() {
        initiateConfigUtils.initSslConfig();
    }
}
