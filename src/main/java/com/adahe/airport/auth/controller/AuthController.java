package com.adahe.airport.auth.controller;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.utils.JwtUtils;
import com.adahe.airport.shared.response.ResponseUtils;
import com.adahe.airport.shared.response.StandardResponse;
import com.adahe.airport.user.domain.User;
import com.adahe.airport.auth.dto.request.LoginRequest;
import com.adahe.airport.auth.dto.response.LoginResponse;
import com.adahe.airport.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {
    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        // Find user by username
        User u = userService.getByUsername(loginRequest.getUsername());
        if (u == null) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "Invalid username or password");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), u.getPassword())) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(u);

        LoginResponse response = new LoginResponse();
        BeanUtils.copyProperties(u, response);
        response.setToken(token);
        return ResponseEntity.ok(ResponseUtils.success(response));
    }
}
