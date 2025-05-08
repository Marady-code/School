package com.jaydee.School.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.jaydee.School.mapper"})
public class MapperConfig {
    // This class ensures that Spring scans and registers all mapper implementations
}