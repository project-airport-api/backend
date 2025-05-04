package com.adahe.airport.auth.filter;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.response.ResponseUtils;
import com.adahe.airport.shared.response.StandardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        StandardResponse<Object> resp = ResponseUtils.error(
                ErrorResponseCode.UNAUTHORISED,
                "Unauthorised access: " + authException.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(resp));
    }
}
