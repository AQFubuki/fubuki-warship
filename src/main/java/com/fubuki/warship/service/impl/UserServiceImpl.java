package com.fubuki.warship.service.impl;

import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.dao.UserMapper;
import com.fubuki.warship.model.pojo.User;
import com.fubuki.warship.service.UserService;
import com.fubuki.warship.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

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

    @Override
    public void register(String userName, String password) throws WarshipException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new WarshipException(WarshipExceptionEnum.NAME_EXISTED);
        }

        //写到数据库
        User user = new User();
        user.setUsername(userName);
        //user.setPassword(password);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws WarshipException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User result = userMapper.selectLogin(userName, md5Password);
        if (result == null) {
            throw new WarshipException(WarshipExceptionEnum.WRONG_PASSWORD);
        }
        return result;

    }

    @Override
    public void updateInformation(User user) throws WarshipException {
        //更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new WarshipException(WarshipExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        //1是普通用户，2是管理员
        return user.getRole().equals(2);
    }
}
