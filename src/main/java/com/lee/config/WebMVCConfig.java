package com.lee.config;

import com.lee.interceptor.AdminInterceptor;
import com.lee.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(redisTemplate))
                .addPathPatterns("/admin").addPathPatterns("/admin/**")
                .addPathPatterns("/article/save").addPathPatterns("/article/update")
                .addPathPatterns("/comment-rest/create")
                .addPathPatterns("/posts-rest/delete/*")
                .addPathPatterns("/approval-rest/**")
                .addPathPatterns("/user/logout")
                .addPathPatterns("/user-rest/**")
                .order(0);//优先执行
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin").addPathPatterns("/admin/**").order(1);
    }
}
