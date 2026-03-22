package com.revworkforce.apigateway.filter;

import com.revworkforce.apigateway.config.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/",
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator",
            "/ws",
            "/ws/"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            sendUnauthorized(response, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.isTokenValid(token)) {
                log.warn("Expired or invalid JWT token for path: {}", path);
                sendUnauthorized(response, "Token is expired or invalid");
                return;
            }

            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            HeaderMutableRequestWrapper wrappedRequest = new HeaderMutableRequestWrapper(request);
            wrappedRequest.addHeader("X-User-Email", email);
            wrappedRequest.addHeader("X-User-Role", role != null ? role : "");

            log.debug("JWT validated — email={}, role={}, path={}", email, role, path);

            filterChain.doFilter(wrappedRequest, response);
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            sendUnauthorized(response, "Token validation failed: " + e.getMessage());
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"success\":false,\"message\":\"" + message + "\"}");
    }
}

