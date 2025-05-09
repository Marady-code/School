package com.jaydee.School.config.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    // Store blacklisted tokens with their expiration time
    private final Map<String, Date> blacklistedTokens = new ConcurrentHashMap<>();
    private final JwtService jwtService;
    
    public void blacklistToken(String token) {
        try {
            Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
            blacklistedTokens.put(token, expiration);
            log.debug("Token added to blacklist, will expire at: {}", expiration);
        } catch (Exception e) {
            log.warn("Failed to blacklist token: {}", e.getMessage());
        }
    }
    
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }
    
    // Clean up expired tokens every hour
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        int initialSize = blacklistedTokens.size();
        
        blacklistedTokens.entrySet().removeIf(entry -> 
            entry.getValue().toInstant().isBefore(now));
        
        int removedTokens = initialSize - blacklistedTokens.size();
        if (removedTokens > 0) {
            log.info("Cleaned up {} expired tokens from blacklist", removedTokens);
        }
    }
}