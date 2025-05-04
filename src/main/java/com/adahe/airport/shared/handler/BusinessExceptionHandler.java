package com.adahe.airport.shared.handler;

import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.response.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BusinessExceptionHandler {
    /**
     * Response 500 internal server error when a BusinessException was caught
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardResponse<Object>> handleBusinessException(BusinessException e) {
        return ResponseEntity.internalServerError().body(new StandardResponse<>(e.getCode(), e.getMessage()));
    }
}
