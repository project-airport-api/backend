package com.adahe.airport.utils;

import com.adahe.airport.user.domain.User;
import com.adahe.airport.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

/**
 * Utility class to create test users
 */
@TestComponent
public class TestDataInitializer {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * Create an admin user for testing
     * @return Created admin user
     */
    public User createAdminUser() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin_password"));
        adminUser.setNickname("Admin User");
        adminUser.setRole("ADMIN");
        adminUser.setAvatar("https://avatar.iran.liara.run/username?username=admin");
        adminUser.setCreateTime(new Date());
        adminUser.setUpdateTime(new Date());
        adminUser.setDeleted(0);

        // Delete user if already exists
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, adminUser.getUsername());
        userMapper.delete(queryWrapper);

        // Insert user
        userMapper.insert(adminUser);

        return adminUser;
    }

    /**
     * Create a regular user for testing
     * @return Created regular user
     */
    public User createRegularUser() {
        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setPassword(passwordEncoder.encode("user_password"));
        regularUser.setNickname("Regular User");
        regularUser.setRole("USER");
        regularUser.setAvatar("https://avatar.iran.liara.run/username?username=user");
        regularUser.setCreateTime(new Date());
        regularUser.setUpdateTime(new Date());
        regularUser.setDeleted(0);

        // Delete user if already exists
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, regularUser.getUsername());
        userMapper.delete(queryWrapper);

        // Insert user
        userMapper.insert(regularUser);

        return regularUser;
    }

    /**
     * Clean up test users
     */
    public void cleanUp() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, "admin_test").or().eq(User::getUsername, "user_test");
        userMapper.delete(queryWrapper);
    }
}