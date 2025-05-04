package com.adahe.airport.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorResponseCode {
    SUCCESS(0, ""),
    INVALID_REQUEST(1, "Invalid request body"),
    UNAUTHORISED(2, "Unauthorised"),
    PERMISSION_DENIED(3, "Permission denied"),
    NOT_FOUND(4, "Not found"),
    SYSTEM_ERROR(5, "System error"),
    ;

    private final int code;
    private final String message;
}
