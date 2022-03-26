package com.example.backend.module.question.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVO implements Serializable {
    private static final long serialVersionUID = 6727716640653485679L;

    /**
     * 回答id
     */
    private String id;

    /**
     * 内容
     */
    private String content;

    /**
     * 所处问题的id
     */
    private String questionId;

    /**
     * 回答者id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 回答时间
     */
    private Date createTime;

    /**
     * 最后一次修改时间
     */
    private Date modifyTime;

    /**
     * 是否被采纳
     */
    private Boolean isAdopted;

    /**
     * 子评论
     */
    private List<AnswerVO> childAnswer;
}
