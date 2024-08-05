package com.linielt.realworldapispringboot;

import com.linielt.realworldapispringboot.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class RealworldApiSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealworldApiSpringBootApplication.class, args);
    }

}
