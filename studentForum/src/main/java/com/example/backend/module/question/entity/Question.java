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
@TableName("question")
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Serializable {
    private static final long serialVersionUID = 1385367477277247968L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不可以为空")
    @TableField(value = "title")
    private String title;
    /**
     * markdown
     */
    @NotBlank(message = "内容不可以为空")
    @TableField("`content`")
    private String content;

    /**
     * 作者ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 赏金
     */
    @Builder.Default
    @TableField("reward")
    private Integer reward = 0;

    /**
     * 浏览数
     */
    @TableField("view")
    @Builder.Default
    private Integer view = 0;

    /**
     * 回答数
     */
    @TableField("ans_num")
    @Builder.Default
    private Integer ansNum = 0;

    /**
     * 是否已经采纳答案
     */
    @TableField("is_solved")
    @Builder.Default
    private Boolean isSolved = false;

    @TableField("is_canceled")
    @Builder.Default
    private Boolean isCanceled = false;

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
