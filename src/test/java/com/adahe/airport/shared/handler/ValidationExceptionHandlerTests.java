package com.adahe.airport.shared.handler;

import com.adahe.airport.api.controller.ApiController;
import com.adahe.airport.api.domain.Api;
import com.adahe.airport.api.dto.request.ApiCreationRequest;
import com.adahe.airport.api.mapper.ApiMapper;
import com.adahe.airport.api.service.ApiService;
import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.enums.RequestMethods;
import com.adahe.airport.shared.response.StandardResponse;
import com.adahe.airport.user.mapper.UserMapper;
import com.adahe.airport.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ApiController.class, ValidationExceptionHandler.class})
public class ValidationExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApiService apiService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private ApiMapper apiMapper;

    @Test
    @DisplayName("Should return 400 Bad Request when request body has validation errors")
    public void testHandleValidationException() throws Exception {
        // Create an invalid request (missing required fields)
        ApiCreationRequest invalidRequest = new ApiCreationRequest();
        invalidRequest.setDescription("Test API Description");

        // Mock service behavior (though it won't be called in this case due to validation failure)
        when(apiService.save(any(Api.class))).thenReturn(true);

        // Perform POST request with invalid data
        MvcResult result = mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.INVALID_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", containsString("API name is mandatory")))
                .andExpect(jsonPath("$.message", containsString("URL is mandatory")))
                .andExpect(jsonPath("$.message", containsString("Request method is mandatory")))
                .andReturn();

        // Additional verification
        String responseContent = result.getResponse().getContentAsString();
        StandardResponse<?> response = objectMapper.readValue(responseContent, StandardResponse.class);

        assertEquals(ErrorResponseCode.INVALID_REQUEST.getCode(), response.getCode());
        assertTrue(response.getMessage().contains("API name is mandatory"));
        assertTrue(response.getMessage().contains("URL is mandatory"));
        assertTrue(response.getMessage().contains("Request method is mandatory"));
    }

    @Test
    @DisplayName("Should process valid request successfully")
    public void testValidRequest() throws Exception {
        // Create a valid request with all required fields
        ApiCreationRequest validRequest = new ApiCreationRequest();
        validRequest.setName("Test API");
        validRequest.setDescription("Test API Description");
        validRequest.setUrl("http://test.api/endpoint");
        validRequest.setRequestMethod(RequestMethods.GET);
        validRequest.setRequestHeader("Content-Type: application/json");
        validRequest.setResponseHeader("Content-Type: application/json");

        // Create Api with ID
        Api savedApi = new Api();
        savedApi.setId(1L);

        // Mock service behavior
        when(apiService.save(ArgumentMatchers.any(Api.class))).thenAnswer(invocation -> {
            Api api = invocation.getArgument(0);
            api.setId(1L); // Set the ID as if it was saved
            return true;
        });

        // Perform POST request with valid data
        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1L))
                .andReturn();
    }
}