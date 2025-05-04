package com.adahe.airport.shared.response;

import com.adahe.airport.shared.enums.ErrorResponseCode;

public class ResponseUtils {
    public static <T> StandardResponse<T> success(T data) {
        return new StandardResponse<>(0, "", data);
    }

    public static <T> StandardResponse<T> error(ErrorResponseCode code) {
        return new StandardResponse<>(code.getCode(), code.getMessage(), null);
    }

    public static <T> StandardResponse<T> error(ErrorResponseCode code, String message) {
        return new StandardResponse<>(code.getCode(), message, null);
    }
}
