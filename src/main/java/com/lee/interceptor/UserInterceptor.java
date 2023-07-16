package com.lee.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lee.pojo.User;
import com.lee.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lee.utils.RedisConstants.USER_LOGIN_KEY;

public class UserInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;
    public UserInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String uuid = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                uuid = cookie.getValue();
            }
        }
        if (StrUtil.isBlank(uuid)){
            return true;
        }
        String json = redisTemplate.opsForValue().get(USER_LOGIN_KEY + uuid);
        if (StrUtil.isBlank(json)){
            return true;
        }
        User user = JSONUtil.toBean(json, User.class);
        UserHolder.saveUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null)return;
        User user = UserHolder.getUser();
        if (user == null){
            modelAndView.addObject("isLogin", false);
            return;
        }
        modelAndView.addObject("loginUser", user);
        modelAndView.addObject("isLogin", true);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
