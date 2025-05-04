package com.adahe.airport.shared.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {
    USER("user"),
    ADMIN("admin"),
    ;

    private final String role;

    @Override
    public String toString() {
        return role;
    }

    @JsonCreator
    public static Roles fromString(String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Invalid role");
        }

        return switch (role.toLowerCase()) {
            case "user" -> USER;
            case "admin" -> ADMIN;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
