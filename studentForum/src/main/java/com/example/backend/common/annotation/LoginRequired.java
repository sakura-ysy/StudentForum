package com.example.backend.common.annotation;

import java.lang.annotation.*;

/**
 * 登录鉴权注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {

    boolean allowAll() default false;  // 允许全部用户访问

    boolean allowCommon() default false;  // 允许普通允许访问

    boolean allowStudent() default false;  // 允许学生才能访问

    boolean allowTeacher() default false;  // 允许教师访问

    boolean allowCompany() default false;  // 允许企业访问

    boolean allowAdmin() default false;  // 允许管理员访问
}
