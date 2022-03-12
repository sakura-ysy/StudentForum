package com.example.backend.common.exception;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.api.IErrorCode;

public class ApiAsserts {
    /**
     * 抛失败异常
     *
     * @param message 说明
     */
    public static void fail(IErrorCode code,String message) {
        throw new ApiException(message);
    }

}
