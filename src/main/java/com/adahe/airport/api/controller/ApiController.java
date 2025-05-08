package com.adahe.airport.api.controller;

import com.adahe.airport.api.domain.Api;
import com.adahe.airport.api.dto.request.ApiCreationRequest;
import com.adahe.airport.api.dto.request.ApiListRequest;
import com.adahe.airport.api.dto.request.ApiUpdateRequest;
import com.adahe.airport.api.service.ApiService;
import com.adahe.airport.shared.enums.Roles;
import com.adahe.airport.shared.enums.SortingOrders;
import com.adahe.airport.shared.exception.BusinessException;
import com.adahe.airport.shared.enums.ErrorResponseCode;
import com.adahe.airport.shared.response.ResponseUtils;
import com.adahe.airport.shared.response.StandardResponse;
import com.adahe.airport.shared.utils.CurrentUserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class ApiController {
    @Resource
    private ApiService apiService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StandardResponse<Page<Api>>> listApiByPage(@Valid ApiListRequest apiListRequest) {
        long page = apiListRequest.getPage();
        long size = apiListRequest.getSize();
        String field = apiListRequest.getField();
        String order = apiListRequest.getOrder();

        Api api = new Api();
        BeanUtils.copyProperties(apiListRequest, api);

        // Supports "like"
        String description = apiListRequest.getDescription();
        api.setDescription(null);

        QueryWrapper<Api> queryWrapper = new QueryWrapper<>(api);
        queryWrapper.like(description != null && !description.isEmpty(), "description", description);
        if (order != null) {
            queryWrapper.orderBy(!order.isEmpty(), order.equals(SortingOrders.ASC.getOrder()), field);
        }
        Page<Api> apiPage = apiService.page(new Page<>(page, size), queryWrapper);

        return ResponseEntity.ok(ResponseUtils.success(apiPage));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<Long>> addApi(@RequestBody @Valid ApiCreationRequest request) {
        Api api = new Api();
        BeanUtils.copyProperties(request, api);

        // Only users with admin role can update an API
        String role = CurrentUserUtils.getCurrentUserRole().toLowerCase();
        if (!role.equals(Roles.ADMIN.getRole())) {
            throw new BusinessException(ErrorResponseCode.PERMISSION_DENIED, "Permission denied");
        }

        // Set created_by with current user
        Long id = CurrentUserUtils.getCurrentUserId();
        api.setCreatedBy(id);

        boolean result = apiService.save(api);
        if (!result) {
            throw new BusinessException(ErrorResponseCode.SYSTEM_ERROR);
        }

        long newApiId = api.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.success(newApiId));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse<Boolean>> updateApi(@PathVariable Long id, @RequestBody @Valid ApiUpdateRequest request) {
        Api api = new Api();
        BeanUtils.copyProperties(request, api);
        api.setId(id);

        // Check if API exists
        Api a = apiService.getById(id);
        if (a == null) {
            throw new BusinessException(ErrorResponseCode.NOT_FOUND, "Api not found");
        }

        // Only users with admin role can update an API
        String role = CurrentUserUtils.getCurrentUserRole().toLowerCase();
        if (!role.equals(Roles.ADMIN.getRole())) {
            throw new BusinessException(ErrorResponseCode.PERMISSION_DENIED, "Permission denied");
        }

        boolean result = apiService.updateById(api);
        return ResponseEntity.ok().body(ResponseUtils.success(result));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteApi(@PathVariable Long id) {
        // Check if API exists
        Api api = apiService.getById(id);
        if (api == null) {
            throw new BusinessException(ErrorResponseCode.NOT_FOUND, "Api not found");
        }

        // Only users with admin role can delete an API
        String role = CurrentUserUtils.getCurrentUserRole().toLowerCase();
        if (!role.equals(Roles.ADMIN.getRole())) {
            throw new BusinessException(ErrorResponseCode.PERMISSION_DENIED, "Permission denied");
        }

        boolean result = apiService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorResponseCode.SYSTEM_ERROR);
        }

        return ResponseEntity.noContent().build();
    }
}
