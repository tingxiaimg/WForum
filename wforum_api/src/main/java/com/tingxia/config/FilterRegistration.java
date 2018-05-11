package com.tingxia.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HttpPutFormContentFilter;

@Configuration
public class FilterRegistration {

    @Bean
    public FilterRegistrationBean testFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpPutFormContentFilter());
        registration.addUrlPatterns("/*");
        // registration.addInitParameter("paramName", "paramValue");
        registration.setName("HttpPutFormContentFilter");
        registration.setOrder(1);
        return registration;
    }
}
