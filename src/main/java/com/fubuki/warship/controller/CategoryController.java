package com.fubuki.warship.controller;

import com.fubuki.warship.common.ApiRestResponse;
import com.fubuki.warship.common.Constant;
import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.pojo.User;
import com.fubuki.warship.model.request.AddCategoryReq;
import com.fubuki.warship.service.CategoryService;
import com.fubuki.warship.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session,
                                      @Valid @RequestBody AddCategoryReq addCategoryReq)
            throws WarshipException {
//        if (addCategoryReq.getName() == null || addCategoryReq.getType() == null
//                || addCategoryReq.getParentId() == null || addCategoryReq.getOrderNum() == null) {
//            return ApiRestResponse.error(WarshipExceptionEnum.PARA_NOT_NULL);
//        }
        User currentUser = (User) session.getAttribute(Constant.FUBUKI_WARSHIP_USER);
        //检查是否登录
        if (currentUser == null) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_LOGIN);
        }
        //检查是否为管理员账户
        if (userService.checkAdminRole(currentUser)) {
            categoryService.addCategory(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_ADMIN);
        }
    }
}
