package com.tingxia.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@ConfigurationProperties(prefix = "wforum.cors")
@Configuration
public class CorsConfig implements WebMvcConfigurer{

    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                .allowedHeaders("*")
                //设置允许跨域请求的域名
                .allowedOrigins(allowedOrigins)
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                .allowCredentials(true)
                //跨域允许时间
                .maxAge(3600);
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
