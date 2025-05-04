package com.adahe.airport.shared.handler;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.response.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {
    /**
     * Responses 401 Bad Request when request body not valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
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
}
