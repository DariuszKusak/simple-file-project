package com.kusak.dariusz.simplefileproject;

import com.kusak.dariusz.simplefileproject.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class SimpleFileProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleFileProjectApplication.class, args);
    }

}
