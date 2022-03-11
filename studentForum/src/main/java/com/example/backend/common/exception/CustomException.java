package com.example.backend.common.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomException extends Exception {

    //可以用来接受我们方法中传的参数
    private Integer id;
    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
