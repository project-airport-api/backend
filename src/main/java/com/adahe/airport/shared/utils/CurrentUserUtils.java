package com.adahe.airport.shared.utils;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class CurrentUserUtils {
    /**
     * Get current user id from request attributes
     * @return Current user id
     */
    public static Long getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Long id = (Long) request.getAttribute("id");

        if (id == null) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "User not authorised");
        }

        return id;
    }

    /**
     * Get current user role from request attributes
     * @return current user role
     */
    public static String getCurrentUserRole() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String role = (String) request.getAttribute("role");

        if (role == null) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "User not authorised");
        }

        return role;
    }

    /**
     * Check if current user is admin
     * @return true if user is admin, false otherwise
     */
    public static boolean isAdmin() {
        String role = getCurrentUserRole();
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Check if current user is a regular user
     * @return true if user has USER role, false otherwise
     */
    public static boolean isUser() {
        String role = getCurrentUserRole();
        return "USER".equalsIgnoreCase(role);
    }

    /**
     * Get current username from Authentication
     * @return Current username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "User not authorised");
        }

        return authentication.getName();
    }
}
