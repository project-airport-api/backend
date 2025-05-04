package com.adahe.airport.user.controller;

import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.response.ResponseUtils;
import com.adahe.airport.shared.response.StandardResponse;
import com.adahe.airport.user.domain.User;
import com.adahe.airport.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * Get current user profile for front-end display
     */
    @GetMapping("/current")
    public ResponseEntity<StandardResponse<User>> getCurrentUser(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        if (id == null) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "User not authorised");
        }

        User u = userService.getById(id);
        if (u == null) {
            throw new BusinessException(ErrorResponseCode.UNAUTHORISED, "User not found");
        }

        u.setPassword(null);
        return ResponseEntity.ok()
                .body(ResponseUtils.success(u));
    }
}
