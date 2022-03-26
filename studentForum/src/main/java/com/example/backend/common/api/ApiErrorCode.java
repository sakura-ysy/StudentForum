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
    COMMENT_FAILED(406,"获取一级评论失败"),
    /**
     * 问题标题已存在
     */
    //QUESTION_TITLE_EXIST(407,"问题的title已存在"),
    /**
     * 积分不足
     */
    USER_SCORE_NOT_ENOUGH(408,"用户积分不足"),
    /**
     * 问题不存在
     */
    QUESTION_NOT_EXIST(409,"问题不存在"),
    /**
     * 非发布者
     */
    AUTHOR_ERROR(410,"非发布者"),
    /**
     * 回答不存在
     */
    ANSWER_NOT_EXIST(411,"回答不存在"),
    /**
     * 问题未撤销
     */
    QUESTION_NOT_CANCELED(412,"问题未撤销"),
    /**
     * 问题已撤销
     */
    QUESTION_ALREADY_CANCELED(413,"问题已撤销"),
    /**
     * 答案不属于该问题
     */
    ANSWER_NOT_BELONG_THIS_QUESTION(414,"答案不属于该问题");

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

