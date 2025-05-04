package com.adahe.airport.auth.dto.response;

import com.adahe.airport.shared.enums.Roles;
import lombok.Data;

@Data
public class LoginResponse {
    /**
     * JWT Token
     */
    private String token;

    private Long id;

    private String username;

    private String nickname;

    private Roles role;

    private String avatar;
}
