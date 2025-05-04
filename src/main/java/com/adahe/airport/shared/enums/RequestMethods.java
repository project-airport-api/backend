package com.adahe.airport.shared.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public enum RequestMethods {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    ;

    private final String method;

    @Override
    public String toString() {
        return method;
    }

    @JsonCreator
    public static RequestMethods fromString(String method) {
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Invalid request method");
        }

        return switch (method.toUpperCase()) {
            case "GET" -> GET;
            case "POST" -> POST;
            case "PUT" -> PUT;
            case "PATCH" -> PATCH;
            case "DELETE" -> DELETE;
            case "HEAD" -> HEAD;
            case "OPTIONS" -> OPTIONS;
            case "TRACE" -> TRACE;
            case "CONNECT" -> CONNECT;
            default -> throw new IllegalArgumentException("Invalid request method");
        };
    }
}
