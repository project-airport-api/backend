package com.adahe.airport.auth.filter;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.response.ResponseUtils;
import com.adahe.airport.shared.response.StandardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate a 401 Unauthorised response for an unauthorised request
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        // Only handle actual authentication exceptions
        if (authException != null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            StandardResponse<Object> errorResponse = new StandardResponse<>(
                    ErrorResponseCode.UNAUTHORISED,
                    "Authentication failed: " + authException.getMessage()
            );

            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
        // Let other exceptions be handled by the global exception handler
    }
}
