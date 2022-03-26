package com.example.backend.module.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@TableName("answer")
@AllArgsConstructor
@NoArgsConstructor
public class Answer implements Serializable {

    private static final long serialVersionUID = -3533960612472130198L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 内容
     */
    @NotBlank(message = "内容不可为空")
    @TableField("`content`")
    private String content;

    /**
     * 所处的问题id
     */
    @TableField(value = "question_id")
    private String questionId;

    /**
     * 评论的回答id
     */
    @TableField(value = "reply_to_id")
    @Builder.Default
    private String replyToId = null;  // 一级为空

    /**
     * 是否被采纳
     */
    @TableField(value = "is_adopted")
    @Builder.Default
    private Boolean isAdopted = false;

    /**
     * 是否已审核
     */
    @TableField("is_audited")
    @Builder.Default
    private Boolean isAudited = true;

    /**
     * 是否审核通过
     */
    @TableField("is_pass")
    @Builder.Default
    private Boolean isPass = true;


    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
}
