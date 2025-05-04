package com.adahe.airport.shared.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortingOrders {
    ASC("asc"),
    DESC("desc");

    private final String order;

    @Override
    public String toString() {
        return order;
    }

    @JsonCreator
    public static SortingOrders fromString(String order) {
        if (order == null || order.isEmpty()) {
            throw new IllegalArgumentException("Invalid sorting order");
        }

        return switch (order.toLowerCase()) {
            case "asc" -> ASC;
            case "desc" -> DESC;
            default -> throw new IllegalArgumentException("Invalid sorting order");
        };
    }
}
