package com.fubuki.warship.controller;

import com.fubuki.warship.common.ApiRestResponse;
import com.fubuki.warship.common.Constant;
import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.pojo.Category;
import com.fubuki.warship.model.pojo.User;
import com.fubuki.warship.model.request.AddCategoryReq;
import com.fubuki.warship.model.request.UpdateCategoryReq;
import com.fubuki.warship.service.CategoryService;
import com.fubuki.warship.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

//@Api(tags = "商品目录模块")
@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    /**
     * 后台添加目录
     *
     * @param session        验证用户信息（仅限管理员使用）
     * @param addCategoryReq Category的新增信息类
     * @return 处理结果
     * @throws WarshipException
     */
//    @ApiOperation("后台添加目录")
    @Operation(summary = "后台添加目录")
    @PostMapping("/admin/category/add")
//    @ApiOperation(value = "后台添加目录")
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

    @Operation(summary = "后台更新目录")
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,
                                          HttpSession session) {
        User currentUser = (User) session.getAttribute(Constant.FUBUKI_WARSHIP_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_LOGIN);
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            categoryService.update(updateCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(WarshipExceptionEnum.NEED_ADMIN);
        }
    }
}
