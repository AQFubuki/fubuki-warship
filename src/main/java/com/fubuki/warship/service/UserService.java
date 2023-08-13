package com.fubuki.warship.service;

import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.model.pojo.User;

/**
 * 描述：     UserService
 */
public interface UserService {

    User getUser();
    void register(String userName, String password) throws WarshipException;
    User login(String userName, String password)throws WarshipException;

    void updateInformation(User user) throws WarshipException;

    boolean checkAdminRole(User user);
}
