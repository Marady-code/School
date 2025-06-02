package com.jaydee.School.config.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import com.jaydee.School.entity.User;

public interface JwtService {
    String generateToken(User user);
    String generateToken(String username);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    boolean validateRefreshToken(String token);
    void invalidateToken(String token);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    long getExpirationTime();
}