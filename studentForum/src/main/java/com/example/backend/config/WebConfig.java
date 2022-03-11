package com.example.backend.config;

import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置中添加拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtUtils jwtUtils;

    /**
     * 添加拦截器
     *
     * @param registry 注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtUtils, userMapper)).addPathPatterns("/**").excludePathPatterns("/user/login");
    }
}