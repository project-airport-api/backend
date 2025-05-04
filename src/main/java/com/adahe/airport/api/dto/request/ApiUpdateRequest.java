package com.adahe.airport.api.dto.request;

import com.adahe.airport.shared.enums.RequestMethods;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApiUpdateRequest {
    /**
     *
     */
    private String name;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String url;

    /**
     *
     */
    private RequestMethods requestMethod;

    /**
     *
     */
    private String requestHeader;

    /**
     *
     */
    private String responseHeader;

    /**
     *
     */
    @Max(1)
    private Integer enabled;
}
