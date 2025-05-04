package com.adahe.airport.user.service;

import com.adahe.airport.user.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ada
* @description Service for table "user"
* @createDate 2025-05-03 21:50:22
*/
public interface UserService extends IService<User> {
    User getByUsername(String username);
}
