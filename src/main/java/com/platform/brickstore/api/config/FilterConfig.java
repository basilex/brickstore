package com.platform.brickstore.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.platform.brickstore.api.exception.NotFoundFilter;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<NotFoundFilter> notFoundFilterRegistration(NotFoundFilter filter) {
        FilterRegistrationBean<NotFoundFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(Integer.MAX_VALUE); // ensure it runs last
        return registration;
    }
}
