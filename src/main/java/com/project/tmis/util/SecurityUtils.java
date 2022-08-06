package com.project.tmis.util;

import com.project.tmis.dto.UserPrinciple;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static Long getCurrentAuthenticatedUserId() {
        UserPrinciple principal = getUserPrincipal();
        if (principal != null) {
            return principal.getId();
        }
        return null;
    }

    private static UserPrinciple getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
            return null;
        }
        return (UserPrinciple) authentication.getPrincipal();
    }
}
