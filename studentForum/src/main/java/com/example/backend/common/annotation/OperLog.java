package com.example.backend.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OperLog {
    String operModul() default "";
    String operType() default "";
    String operDesc() default "";
}
