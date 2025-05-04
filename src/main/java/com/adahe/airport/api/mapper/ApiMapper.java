package com.adahe.airport.api.mapper;

import com.adahe.airport.api.domain.Api;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Ada He
 * @description MyBatis mapper for table "api"
 * @createDate 2025-05-03 21:49:56
 * @Entity com.adahe.airport.api.domain.Api
 */
@Mapper
public interface ApiMapper extends BaseMapper<Api> {

}
