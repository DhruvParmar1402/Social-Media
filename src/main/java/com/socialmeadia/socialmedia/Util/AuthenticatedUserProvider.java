package com.socialmeadia.socialmedia.Util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuthenticatedUserProvider {

    private final String userName;
    private final String role;


    public AuthenticatedUserProvider(HttpServletRequest request, JwtUtil jwtUtil) {
        String token = extractToken(request);
        this.userName = jwtUtil.extractUserId(token);
        this.role = jwtUtil.extractRole(token);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Authorization token missing or invalid.");
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }
}
