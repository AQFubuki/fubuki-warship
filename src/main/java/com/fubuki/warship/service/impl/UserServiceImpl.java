package com.fubuki.warship.service.impl;

import com.fubuki.warship.model.dao.UserMapper;
import com.fubuki.warship.model.pojo.User;
import com.fubuki.warship.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：     UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1L);
    }
}
