package com.jaydee.School.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jaydee.School.config.jwt.JwtAuthenticationFilter;
import com.jaydee.School.config.ratelimit.RateLimitFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	private final RateLimitFilter rateLimitFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter)
			throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// Public endpoints with rate limiting
						.requestMatchers("/api/auth/parent/verify-student").permitAll()
						.requestMatchers("/api/auth/parent/register").permitAll().requestMatchers("/auth/login")
						.permitAll().requestMatchers("/auth/refresh-token").permitAll()

						// Documentation
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

						// Static resources
						.requestMatchers("/error", "/error/**").permitAll()
						.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()

						// Role-based endpoints
						.requestMatchers("/api/admin/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
						.requestMatchers("/api/teachers/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER")
						.requestMatchers("/api/students/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER", "STUDENT")
						.requestMatchers("/api/parents/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "PARENT")

						// All other endpoints require authentication
						.anyRequest().authenticated())
				.exceptionHandling(handling -> handling.authenticationEntryPoint((request, response, authException) -> {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Authentication required\"}");
				}).accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setContentType("application/json");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"Access denied\"}");
				})).authenticationProvider(authenticationProvider)
				.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Add your frontend URL
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
		configuration.setExposedHeaders(Arrays.asList("Authorization"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}