package com.fubuki.warship.controller;

import com.fubuki.warship.common.ApiRestResponse;
import com.fubuki.warship.common.Constant;
import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.pojo.User;
import com.fubuki.warship.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：     用户控制器
 */
@Controller
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password)
            throws WarshipException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(WarshipExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName, password);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password,
                                 HttpSession session)
            throws WarshipException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_PASSWORD);
        }

        User user = userService.login(userName, password);
        //保存用户信息时不返回密码
        user.setPassword(null);
        session.setAttribute(Constant.FUBUKI_WARSHIP_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session
            , @RequestParam String signature)
            throws WarshipException {
        //根据session信息判断当前用户
        User currentUser = (User) session.getAttribute(Constant.FUBUKI_WARSHIP_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session) throws WarshipException {
        session.removeAttribute(Constant.FUBUKI_WARSHIP_USER);
        return ApiRestResponse.success();
    }

    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName,
                                      @RequestParam("password") String password,
                                      HttpSession session)
            throws WarshipException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        user.setPassword(null);
        if (userService.checkAdminRole(user)) {
            session.setAttribute(Constant.FUBUKI_WARSHIP_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_ADMIN);
        }
    }
}
