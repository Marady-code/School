package com.jaydee.School.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jaydee.School.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username or email: {}", username);
        
        return userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> {
                log.warn("User with username or email '{}' not found", username);
                return new UsernameNotFoundException("User not found with username or email: " + username);
            });
    }
}