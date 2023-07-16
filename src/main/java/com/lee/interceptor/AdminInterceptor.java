package com.lee.interceptor;

import com.lee.pojo.User;
import com.lee.utils.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.err.println("456456456");
        User user = UserHolder.getUser();
        if (!user.getRole().equals("ADMIN")){
            response.sendRedirect("/classwork");
            return false;
        }
        return true;
    }
}
