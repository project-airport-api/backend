package com.adahe.airport.auth.filter;

import com.adahe.airport.auth.service.CustomUserDetailsService;
import com.adahe.airport.shared.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private CustomUserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Filtering {}", request.getRequestURI());
        try {
            // Try to parse JWT token and extract username from token
            String token = parseToken(request);
            if (token != null) {
                String username = jwtUtils.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Try to load user from DB by username
                    UserDetails ud = userDetailService.loadUserByUsername(username);
                    if (jwtUtils.validateToken(token, ud)) {
                        // Generate authentication details for this user
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                ud, null, ud.getAuthorities()
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        request.setAttribute("id", jwtUtils.extractUserId(token));
                        request.setAttribute("role", jwtUtils.extractRole(token).toUpperCase());
                    } // if (jwtUtils.validateToken)
                } // if (username != null && ...)
            } // if (token != null)
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/login");
    }

    /**
     * Parse JWT from Authorization header (X-Token)
     */
    private String parseToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
