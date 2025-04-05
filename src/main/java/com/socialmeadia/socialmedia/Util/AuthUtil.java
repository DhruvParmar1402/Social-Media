package com.socialmeadia.socialmedia.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtil {

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User is not authenticated");
        }
        return authentication.getName();
    }

    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            throw new RuntimeException("User role not found");
        }

        Optional<? extends GrantedAuthority> role = authentication.getAuthorities().stream().findFirst();
        return role.map(GrantedAuthority::getAuthority).orElseThrow(
                () -> new RuntimeException("Invalid role")
        );
    }

    public void ensureInstructor() {
        if (!"INSTRUCTOR".equals(getCurrentUserRole())) {
            throw new RuntimeException("Only instructors are allowed to perform this action.");
        }
    }
}
