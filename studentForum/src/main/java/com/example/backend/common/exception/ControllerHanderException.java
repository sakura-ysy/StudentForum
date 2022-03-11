package com.example.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

// @ControllerAdvice注解表示这个Controller不处理http请求，只处理当其他controller抛出异常时，进行处理。
@ControllerAdvice
public class ControllerHanderException {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //在这个方法里定义我们需要返回的格式
    public Map<String, Object> handleUserNotExistException(CustomException ex){
        Map<String, Object> result = new HashMap<>();
        result.put("code", ex.getId());
        result.put("message", ex.getMessage());
        return result;
    }


}