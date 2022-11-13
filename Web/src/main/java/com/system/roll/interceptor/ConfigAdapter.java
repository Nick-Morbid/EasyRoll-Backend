package com.system.roll.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/*
 * Author:陈宏侨
 * Edu:Fuzhou University
 * Date:2021/10/18
 * Project:Okr Leader
 * */
@Configuration
public class ConfigAdapter implements WebMvcConfigurer {
    @Resource
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/student/base/login/auth","/supervisor/base/login/auth","/professor/login/auth","/professor/nonparty/auth");
    }
}
