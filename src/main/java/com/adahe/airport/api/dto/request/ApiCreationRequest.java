package com.adahe.airport.api.dto.request;

import com.adahe.airport.shared.enums.RequestMethods;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiCreationRequest {
    /**
     *
     */
    @NotBlank(message = "API name is mandatory")
    private String name;

    /**
     *
     */
    private String description;

    /**
     *
     */
    @NotBlank(message = "URL is mandatory")
    private String url;

    /**
     *
     */
    @NotNull(message = "Request method is mandatory")
    private RequestMethods requestMethod;

    /**
     *
     */
    private String requestHeader;

    /**
     *
     */
    private String responseHeader;
}
