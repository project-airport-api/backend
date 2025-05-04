package com.adahe.airport.api.dto.request;

import com.adahe.airport.shared.enums.RequestMethods;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApiListRequest {
    /**
     *
     */
    @Size(min = 1)
    private Long id;

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
    @Size(max = 1)
    private Integer enabled;
}
