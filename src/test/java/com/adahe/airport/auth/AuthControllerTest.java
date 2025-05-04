package com.adahe.airport.auth;

import com.adahe.airport.api.mapper.ApiMapper;
import com.adahe.airport.api.service.ApiService;
import com.adahe.airport.auth.controller.AuthController;
import com.adahe.airport.auth.dto.request.LoginRequest;
import com.adahe.airport.auth.dto.response.LoginResponse;
import com.adahe.airport.auth.service.CustomUserDetailsService;
import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.enums.Roles;
import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.utils.JwtUtils;
import com.adahe.airport.user.domain.User;
import com.adahe.airport.user.mapper.UserMapper;
import com.adahe.airport.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private ApiMapper apiMapper;

    @MockitoBean
    private ApiService apiService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Login endpoint should return token when credentials are valid")
    public void testLoginSuccess() throws Exception {
        User testUser = new User();
        testUser.setId(1L)
                .setUsername("test_user")
                .setPassword("password")
                .setNickname("Test User")
                .setRole(Roles.USER.getRole());

        // Create login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test_user");
        loginRequest.setPassword("password");

        // Mock service response
        LoginResponse loginResponse = new LoginResponse();
        BeanUtils.copyProperties(testUser, loginResponse);
        loginResponse.setToken("jwt.token.string");

        when(userService.getByUsername(any(String.class))).thenReturn(testUser);

        // Perform login request
        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("jwt.token.string"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("test_user"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    @DisplayName("Login endpoint should return 401 when credentials are invalid")
    public void testLoginFailure() throws Exception {
        // Create login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test_user");
        loginRequest.setPassword("wrong_password");

        // Mock service exception
        when(userService.getByUsername(any(String.class))).thenThrow(
                new BusinessException(ErrorResponseCode.UNAUTHORISED, "Invalid username or password")
        );

        // Perform login request
        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.UNAUTHORISED.getCode()))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    @DisplayName("Login endpoint should return 401 when request is missing required fields")
    public void testLoginValidationFailure() throws Exception {
        // Create invalid login request (missing password)
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test_user");
        // password is null

        // Perform login request
        mockMvc.perform(post("/api/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.UNAUTHORISED.getCode()))
                .andExpect(jsonPath("$.message").exists());
    }
}