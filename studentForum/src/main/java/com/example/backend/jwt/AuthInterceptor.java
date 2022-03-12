package com.example.backend.jwt;

import com.example.backend.common.api.IErrorCode;
import com.google.gson.Gson;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.exception.ApiAsserts;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.UserMapper;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.example.backend.common.api.ApiErrorCode.*;

/**
 * 登录鉴权拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private static final Gson gson = new Gson();

    public AuthInterceptor(JwtUtils jwtUtils, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(LoginRequired.class)) {
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            String userId = jwtUtils.getUserIdFromToken(request);
            if (userId.equals("")){
                sendJsonMessage(response,UNAUTHORIZED);
            }
            else {
                User user = userMapper.selectById(userId);
                if (user == null) {
                    sendJsonMessage(response,USER_NOT_EXISTS);
                    //ApiAsserts.fail(USER_NOT_EXISTS, "用户不存在");
                }
                currentUser.set(user);
                // 允许全部通过
                if (loginRequired.allowAll()) return true;
                // 允许普通用户通过
                if (loginRequired.allowCommon() && authCommon(user)) return true;
                // 允许学生通过
                if (loginRequired.allowStudent() && authStudent(user)) return true;
                // 允许教师通过
                if (loginRequired.allowTeacher() && authTeacher(user)) return true;
                // 允许企业通过
                if (loginRequired.allowCommon() && authCompany(user)) return true;
                sendJsonMessage(response,ROLE_FORBIDDEN);
                return false;
            }
        }
        return true;
    }

    // 检验是否为普通用户
    private boolean authCommon(User user) throws ApiException {
        return user.getRole() == 4;
    }

    // 检验是否为学生
    private boolean authStudent(User user) throws ApiException {
        return user.getRole() == 1;
    }

    // 检验是否为教师
    private boolean authTeacher(User user) throws ApiException {
        return user.getRole() == 2;
    }

    // 检验是否为企业
    private boolean authCompany(User user) throws ApiException {
        return user.getRole() == 3;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * 释放ThreadLocal中的数据
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @param ex       异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        currentUser.remove();
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    /**
     * 响应数据给前端
     * @param response
     * @param code
     */
    public static void sendJsonMessage(HttpServletResponse response, IErrorCode code) throws IOException {
        Map<String,Object> errorMap = new HashMap<>();
        errorMap.put("code",code.getCode());
        errorMap.put("message",code.getMessage());
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(gson.toJson(errorMap));
        writer.close();
        response.flushBuffer();
    }

}
