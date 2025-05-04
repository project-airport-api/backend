package com.adahe.airport.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.adahe.airport.user.domain.User;
import com.adahe.airport.user.service.UserService;
import com.adahe.airport.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author ada
* @description Service Implementation for table "user"
* @createDate 2025-05-03 21:50:22
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }
}




