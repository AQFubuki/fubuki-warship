package com.fubuki.warship.config;

import com.fubuki.warship.filter.AdminFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：     Admin过滤器的配置
 */
@Configuration
public class AdminFilterConfig {

    private final AdminFilter adminFilter;

    @Autowired
    public AdminFilterConfig(AdminFilter adminFilter) {
        this.adminFilter = adminFilter;
    }
//    @Bean
//    public AdminFilter adminFilter() {
//        return new AdminFilter(userService);
//    }

    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //filterRegistrationBean.setFilter(adminFilter());
        filterRegistrationBean.setFilter(this.adminFilter);
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        filterRegistrationBean.setName("adminFilterConf");
        return filterRegistrationBean;
    }
}
