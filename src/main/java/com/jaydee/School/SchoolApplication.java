package com.jaydee.School;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.jaydee.School.entity")
@EnableJpaRepositories(basePackages = "com.jaydee.School.repository")
public class SchoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }
}
