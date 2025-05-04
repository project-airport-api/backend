package com.adahe.airport.shared.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageRequest {
    @Min(1)
    @NotNull(message = "Page number is mandatory")
    private Long page;

    @Min(1)
    @NotNull(message = "Page size is mandatory")
    private Long size;

    /**
     * Sorting by this field
     */
    private String field;

    /**
     * Sorting order (asc or desc)
     */
    private String order;
}
