package com.fubuki.warship.filter;

import com.fubuki.warship.common.Constant;
import com.fubuki.warship.model.pojo.User;
import com.fubuki.warship.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AdminFilter implements Filter {

    private final UserService userService;
    @Autowired
    public AdminFilter(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        //从request中获取session
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constant.FUBUKI_WARSHIP_USER);
        if (currentUser == null) {
            //return ApiRestResponse.error(WarshipExceptionEnum.NEED_LOGIN);
            PrintWriter out = new HttpServletResponseWrapper(
                    (HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10007,\n"
                    + "    \"msg\": \"NEED_LOGIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
            return;
        }
        //校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //是管理员，执行操作
            filterChain.doFilter(servletRequest, servletResponse);
            //categoryService.update(updateCategoryReq);
            //return ApiRestResponse.success();
        } else {
            //return ApiRestResponse.error(WarshipExceptionEnum.NEED_ADMIN);
            PrintWriter out = new HttpServletResponseWrapper(
                    (HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10009,\n"
                    + "    \"msg\": \"NEED_ADMIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
