package com.nutcracker.config.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

/**
 * custom filter
 * @author 胡桃夹子
 * @date 2022/12/22 15:16
 */
@Configuration
public class CustomFilterConfig {

    /*@Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(accessAuthFilter());
        registration.setName("accessAuthFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(Constants.ACCESS_AUTH_FILTER_ORDER);
        return registration;
    }*/

    // 注册多个filter
    /*@Bean
    public FilterRegistrationBean customFilterRegistrationBean(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(customFilter());
        registration.setName("customFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(9);
        return registration;
    }*/


    @Bean
    public Filter accessAuthFilter() {
        return new AccessAuthFilter();
    }

}
