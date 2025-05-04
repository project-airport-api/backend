package com.adahe.airport.user.mapper;

import com.adahe.airport.user.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Ada He
 * @description MyBatis mapper for table "user"
 * @createDate 2025-05-03 21:50:22
 * @Entity com.adahe.airport.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getByUsername(@Param("username") String username);
}
