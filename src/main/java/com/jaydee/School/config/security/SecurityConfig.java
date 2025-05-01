//package com.jaydee.School.config.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
////    private final JwtAuthenticationFilter jwtAuthFilter;
//    //private final UserDetailsServiceImpl userDetailsService;
//
////    @Autowired
////    private PasswordEncoder passwordEncoder;
//    
//    private final UserDetailsServiceImpl userDetailsService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                // Public endpoints
//                .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**", "/api/auth/**").permitAll()
//                
//                // Admin only endpoints
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                
//                // Resource-specific permissions
////                .requestMatchers("/api/students/**").hasAuthority("student:read")
////                .requestMatchers("/api/students/*/update", "/api/students/*/delete").hasAuthority("student:write")
////                
////                .requestMatchers("/api/teachers/**").hasAuthority("teacher:read")
////                .requestMatchers("/api/teachers/*/update", "/api/teachers/*/delete").hasAuthority("teacher:write")
////                
////                .requestMatchers("/api/classes/**").hasAuthority("class:read")
////                .requestMatchers("/api/classes/*/update", "/api/classes/*/delete").hasAuthority("class:write")
////                
////                .requestMatchers("/api/subjects/**").hasAuthority("subject:read")
////                .requestMatchers("/api/subjects/*/update", "/api/subjects/*/delete").hasAuthority("subject:write")
////                
////                .requestMatchers("/api/attendance/**").hasAuthority("attendance:read")
////                .requestMatchers("/api/attendance/*/update", "/api/attendance/*/delete").hasAuthority("attendance:write")
////                
////                .requestMatchers("/api/exam-results/**").hasAuthority("exam_result:read")
////                .requestMatchers("/api/exam-results/*/update", "/api/exam-results/*/delete").hasAuthority("exam_result:write")
////                
////                .requestMatchers("/api/reports/**").hasAuthority("report:read")
////                .requestMatchers("/api/reports/*/update", "/api/reports/*/delete").hasAuthority("report:write")
////                
////                .requestMatchers("/api/timetable/**").hasAuthority("timetable:read")
////                .requestMatchers("/api/timetable/*/update", "/api/timetable/*/delete").hasAuthority("timetable:write")
////                
////                .requestMatchers("/api/users/**").hasAuthority("user:read")
////                .requestMatchers("/api/users/*/update", "/api/users/*/delete").hasAuthority("user:write")
//                
//                // Any other request requires authentication
//                .anyRequest()
//                .authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")
//                .permitAll()
//            );
//
//        return http.build();
//    }    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//
////    @Bean
////    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
////        return config.getAuthenticationManager();
////    }
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////            .authorizeHttpRequests((authz) -> authz
////            	.requestMatchers("/","index.html","css/**","js/**").permitAll()
////                .requestMatchers("/api/admin/**").hasRole("ADMIN")
////                .requestMatchers("/api/user/**").hasRole("USER")
////                .anyRequest()
////                .authenticated()
////            );
////        return http.build();
////    }
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
////    @Bean
////    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
////    	return authenticationConfiguration.getAuthenticationManager();
////    }
//        	
////            .csrf(csrf -> csrf.disable())
////            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////            .authorizeHttpRequests(auth -> auth
////                .requestMatchers("/api/auth/**").permitAll()
////                .requestMatchers("/api/public/**").permitAll()
////                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
////                .anyRequest()
////                .authenticated()
//        	
////            .sessionManagement(session -> session
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////            )
////            .headers(headers -> headers
////                .frameOptions(frame -> frame.sameOrigin())
////                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
////            )
////            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//    	
//    		
//    
////    @Bean
////    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
////    	return authenticationConfiguration.getAuthenticationManager();
////    }
////    
////    public void configure(AuthenticationManagerBuilder auth) throws Exception{
////    	auth.authenticationProvider(getAu)
////    }
//
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.addAllowedOrigin("*");
////        configuration.addAllowedMethod("*");
////        configuration.addAllowedHeader("*");
////        
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration);
////        return source;
////    }
//}
