package com.adahe.airport.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.adahe.airport.api.domain.Api;
import com.adahe.airport.api.service.ApiService;
import com.adahe.airport.api.mapper.ApiMapper;
import org.springframework.stereotype.Service;

/**
* @author ada
* @description Service for table "api"
* @createDate 2025-05-03 21:49:56
*/
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api>
    implements ApiService{

}




