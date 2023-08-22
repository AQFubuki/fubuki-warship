package com.fubuki.warship.config;


import com.fubuki.warship.filter.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：     User过滤器的配置
 */
@Configuration
public class UserFilterConfig {
    private final UserFilter userFilter;
    @Autowired
    public UserFilterConfig(UserFilter userFilter) {
        this.userFilter = userFilter;
    }




    @Bean(name = "userFilterConf")
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(this.userFilter);
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/order/*");
        filterRegistrationBean.setName("userFilterConf");
        return filterRegistrationBean;
    }
}
