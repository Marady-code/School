package com.jaydee.School.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOriginPatterns("http://localhost:3000", "http://localhost:3001", "http://localhost:4200", "http://localhost:4201")
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true);
	}
}
