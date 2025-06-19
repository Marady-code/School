package com.jaydee.School.config.ratelimit;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final int WINDOW_SIZE_MS = 60 * 1000; // 1 minute

    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Only apply rate limiting to public endpoints
        if (isPublicEndpoint(request.getRequestURI())) {
            String clientIp = getClientIp(request);
            RequestCounter counter = requestCounters.computeIfAbsent(clientIp, k -> new RequestCounter());

            if (!counter.tryIncrement()) {
                log.warn("Rate limit exceeded for IP: {}", clientIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Please try again later.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.startsWith("/api/auth/parent/") || 
               uri.startsWith("/auth/login") || 
               uri.startsWith("/auth/refresh-token");
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long windowStart = System.currentTimeMillis();

        public synchronized boolean tryIncrement() {
            long now = System.currentTimeMillis();
            if (now - windowStart >= WINDOW_SIZE_MS) {
                // Reset counter for new window
                count.set(0);
                windowStart = now;
            }
            return count.incrementAndGet() <= MAX_REQUESTS_PER_MINUTE;
        }
    }
} 