package com.adahe.airport.shared.handler;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.response.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Responses 401 Bad Request when request body not valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardResponse<Object>> handleRequestBodyValidationException(MethodArgumentNotValidException e) {
        StringBuilder builder = new StringBuilder();
        builder.append("Invalid request arguments: ");
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            builder.append(field).append(": ").append(errorMessage).append(";\n");
        });

        return ResponseEntity
                .badRequest()
                .body(new StandardResponse<>(ErrorResponseCode.INVALID_REQUEST, builder.toString()));
    }

    /**
     * Response 500 internal server error when a BusinessException was caught
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardResponse<Object>> handleBusinessException(BusinessException e) {
        return ResponseEntity.internalServerError().body(new StandardResponse<>(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        // This is a legitimate authorization issue - return 403
        StandardResponse<Object> response = new StandardResponse<>(
                ErrorResponseCode.UNAUTHORISED,
                "Access denied: You don't have permission to access this resource"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        // This is a legitimate authentication issue - return 401
        StandardResponse<Object> response = new StandardResponse<>(
                ErrorResponseCode.UNAUTHORISED,
                "Authentication required: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Handle all other exceptions as 500 errors or their appropriate status
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Object>> handleAllExceptions(Exception ex) {
        // Log the exception for debugging
        log.error("Unhandled exception occurred", ex);

        // Create a generic error response
        StandardResponse<Object> response = new StandardResponse<>(
                ErrorResponseCode.SYSTEM_ERROR,
                "An unexpected error occurred: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}