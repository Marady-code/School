package com.jaydee.School.config.jwt;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.jaydee.School.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
    
    @Value("${jwt.secret-key}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateToken(User user) {
        return generateToken(user.getUsername());
    }

    @Override
    public String extractUsername(String token) {
        return getUsernameFromToken(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
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
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public long getExpirationTime() {
        return jwtExpiration;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}