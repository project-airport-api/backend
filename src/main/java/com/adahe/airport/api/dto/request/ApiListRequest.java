package com.adahe.airport.api.dto.request;

import com.adahe.airport.shared.enums.RequestMethods;
import com.adahe.airport.shared.request.PageRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiListRequest extends PageRequest implements Serializable {
    /**
     *
     */
    @Min(1)
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
    @Max(1)
    private Integer enabled;

    /**
     *
     */
    @Min(1)
    private Long createdBy;
}
