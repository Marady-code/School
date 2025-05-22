package com.jaydee.School.config.jwt;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.jaydee.School.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
    
    private final SecretKey secretKey;
    
    @Value("${jwt.expiration:2592000000}")  // Default: 30 days in milliseconds
    private long jwtExpiration;
    
    public JwtServiceImpl(SecretKey jwtSecretKey) {
        this.secretKey = jwtSecretKey;
    }

    @Override
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateToken(User user) {
        return generateToken(user.getEmail()); // Use email directly instead of getUsername()
    }

    @Override
    public String extractUsername(String token) {
        return getUsernameFromToken(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    @Override
    public void invalidateToken(String token) {
        // Implementation for token blacklisting would go here
        // Could use Redis or a database table to store invalidated tokens
    }    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String emailFromToken = extractUsername(token);
        // Since we're using email in token generation, we need to compare with email here
        // Assume userDetails is actually User instance that contains email
        String email = "";
        if (userDetails instanceof User) {
            email = ((User) userDetails).getEmail();
        } else {
            email = userDetails.getUsername(); // Fallback
        }
        
        boolean isValid = (emailFromToken.equals(email) && !isTokenExpired(token));
        
        // Add debug logging
        System.out.println("Token validation - Email from token: " + emailFromToken);
        System.out.println("Token validation - Email from UserDetails: " + email);
        System.out.println("Token validation - Token expired: " + isTokenExpired(token));
        System.out.println("Token validation - Result: " + isValid);
        return isValid;
    }@Override
    public long getExpirationTime() {
        // This returns the configured JWT expiration time (30 days)
        return jwtExpiration;
    }private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}