package com.adahe.airport.shared.exception;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorResponseCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorResponseCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
