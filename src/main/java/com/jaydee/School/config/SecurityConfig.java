package com.jaydee.School.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/v3/api-docs/**", 
//                               "/swagger-ui/**", 
//                               "/swagger-ui.html")
//                .permitAll()
//                .anyRequest()
//                .authenticated());
//        return http.build();
//    }
	
	@Configuration 
	public class SecurityConfig1 {
	   @Bean
	    public WebSecurityCustomizer webSecurityCustomizer() {
	        return (web) -> web.ignoring()
	          .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**");
	    }
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/**").permitAll() // Allow access to all APIs
	        )
	        .csrf(csrf -> csrf.disable()) // Disable CSRF for non-browser clients (like Postman)
	        .formLogin(login -> login.disable()); // Disable default login form

	    return http.build();
	}


}
