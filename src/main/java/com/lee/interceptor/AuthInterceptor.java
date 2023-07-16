package com.lee.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lee.pojo.User;
import com.lee.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lee.utils.RedisConstants.USER_LOGIN_KEY;


public class AuthInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;
    public AuthInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("789789789");
//        Cookie[] cookies = request.getCookies();
//        String uuid = null;
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("token")){
//                uuid = cookie.getValue();
//            }
//        }
//        if (StrUtil.isBlank(uuid)){
//            response.sendRedirect("/classwork");
//            return false;
//        }
//        String json = redisTemplate.opsForValue().get(USER_LOGIN_KEY + uuid);
//        if (StrUtil.isBlank(json)){
//            response.sendRedirect("/classwork");
//            return false;
//        }
//        User user = JSONUtil.toBean(json, User.class);
//        UserHolder.saveUser(user);
        User user = UserHolder.getUser();
        if (user == null){
            response.sendRedirect("/classwork");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("101010101010");
        UserHolder.removeUser();
    }
}
