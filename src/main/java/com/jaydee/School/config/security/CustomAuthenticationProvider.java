//package com.jaydee.School.config.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.jaydee.School.DTO.UserDTO;
//import com.jaydee.School.service.UserService;
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        UserDTO user = userService.getUserByUsername(username);
//
//        if (passwordEncoder.matches(password, user.getPassword())) {
//            UserPrincipal userDetails = new UserPrincipal(user);
//            return new UsernamePasswordAuthenticationToken(
//                userDetails, password, userDetails.getAuthorities());
//        }
//
//        throw new BadCredentialsException("Invalid username or password");
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//} 
//
//
