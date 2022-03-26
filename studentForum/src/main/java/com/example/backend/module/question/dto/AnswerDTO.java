package com.example.backend.module.question.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerDTO implements Serializable {
    private static final long serialVersionUID = 7650675226002743409L;

    /**
     * 所处问题的id
     */
    private String questionId;

    /**
     * 内容
     */
    private String content;

    /**
     * 回复的回答id，一级回答为空
     */
    private String replyToId;
}
