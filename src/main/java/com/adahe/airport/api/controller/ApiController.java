package com.adahe.airport.api.controller;

import com.adahe.airport.api.domain.Api;
import com.adahe.airport.api.dto.request.ApiCreationRequest;
import com.adahe.airport.api.dto.request.ApiUpdateRequest;
import com.adahe.airport.api.service.ApiService;
import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.response.ResponseUtils;
import com.adahe.airport.shared.response.StandardResponse;
import com.adahe.airport.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class ApiController {
    @Resource
    private ApiService apiService;

    @Resource
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StandardResponse<Long>> addApi(@RequestBody @Valid ApiCreationRequest request) {
        Api api = new Api();
        BeanUtils.copyProperties(request, api);

        // FIXME: For development only
        api.setCreatedBy(1L);

        boolean result = apiService.save(api);
        if (!result) {
             throw new BusinessException(ErrorResponseCode.SYSTEM_ERROR);
        }

        long newApiId = api.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.success(newApiId));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StandardResponse<Boolean>> updateApi(@PathVariable Long id, @RequestBody @Valid ApiUpdateRequest request) {
        Api api = new Api();
        BeanUtils.copyProperties(request, api);
        api.setId(id);

        // Check if API exists
        Api a = apiService.getById(id);
        if (a == null) {
            throw new BusinessException(ErrorResponseCode.NOT_FOUND, "Api not found");
        }

        // FIXME: Only admin or creator can update an API
        boolean result = apiService.updateById(api);
        return ResponseEntity.ok().body(ResponseUtils.success(result));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteApi(@PathVariable Long id) {
        // Check if API exists
        Api api = apiService.getById(id);
        if (api == null) {
            throw new BusinessException(ErrorResponseCode.NOT_FOUND, "Api not found");
        }

        // FIXME: Only admin or creator can delete an API
        boolean result = apiService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorResponseCode.SYSTEM_ERROR);
        }

        return ResponseEntity.noContent().build();
    }
}
