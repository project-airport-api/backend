package com.adahe.airport.shared.response;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public StandardResponse(int code, T data) {
        this.code = code;
        this.data = data;
        this.message = "";
    }

    public StandardResponse(ErrorResponseCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public StandardResponse(ErrorResponseCode code, String message) {
        this.code = code.getCode();
        this.message = message;
        this.data = null;
    }
}
