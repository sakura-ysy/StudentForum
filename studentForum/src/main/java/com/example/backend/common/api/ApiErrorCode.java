package com.example.backend.common.api;

public enum ApiErrorCode implements IErrorCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 失败
     */
    FAILED(-1, "操作失败"),
    /**
     * 未登录，Token过期
     */
    UNAUTHORIZED(401, "token错误或token已经过期"),
    /**
     * 角色无权限
     */
    ROLE_FORBIDDEN(402, "角色无此权限"),
    /**
     * 参数校验错误
     */
    VALIDATE_FAILED(403, "参数检验失败"),
    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(404,"用户不存在"),
    /**
     * 帖子不存在
     */
    POST_NOT_EXISTS(405,"帖子不存在"),
    /**
     * 获取评论失败
     */
    COMMENT_FAILED(406,"获取一级评论失败");

    private final Integer code;
    private final String message;

    ApiErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApiErrorCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

