//package com.jaydee.School.security;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.UnsupportedJwtException;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//
//@Component
//public class JwtTokenProvider {
//
//	@Value("${spring.security.jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${app.jwt.expiration}")
//    private int jwtExpirationInMs;
//
//    private Key key;
//
//    @PostConstruct
//    public void init() {
//        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
//    }
//
//    public String generateToken(Authentication authentication) {
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//
//        String roles = userPrincipal.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        return Jwts.builder()
//                .setSubject(Long.toString(userPrincipal.getId()))
//                .claim("roles", roles)
//                .claim("username", userPrincipal.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(expiryDate)
//                .signWith(key)
//                .compact();
//    }
//
//    public Long getUserIdFromJWT(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Long.parseLong(claims.getSubject());
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
//            return true;
//        } catch (SecurityException ex) {
//            // Invalid JWT signature
//            return false;
//        } catch (MalformedJwtException ex) {
//            // Invalid JWT token
//            return false;
//        } catch (ExpiredJwtException ex) {
//            // Expired JWT token
//            return false;
//        } catch (UnsupportedJwtException ex) {
//            // Unsupported JWT token
//            return false;
//        } catch (IllegalArgumentException ex) {
//            // JWT claims string is empty
//            return false;
//        }
//    }
//} 


