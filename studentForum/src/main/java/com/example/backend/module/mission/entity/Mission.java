package com.example.backend.module.mission.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@TableName("mission")
@AllArgsConstructor
@NoArgsConstructor
public class Mission implements Serializable {

    private static final long serialVersionUID = 4922663593069261192L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 内容
     */
    @NotBlank(message = "内容不可以为空")
    @TableField(value = "content")
    private String content;

    /**
     * 标题
     */
    @NotBlank(message = "内容不可以为空")
    @TableField(value = "title")
    private String title;

    /**
     * 发布者ID
     */
    @TableField("user_id")
    private String userId;  // 评论者id

    /**
     * 奖励金额
     */
    @NotBlank(message = "内容不可以为空")
    @TableField(value = "reward")
    private String reward;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 截止时间
     */
    @TableField(value = "dead_time", fill = FieldFill.UPDATE)
    private Date deadTime;

    /**
     * 目前人数
     */
    @Builder.Default
    @TableField("now_sum")
    private Integer nowSum = 0;
    /**
     * 限制人数
     */
    @TableField(value = "sum_limit")
    private Integer sumLimit;

    /**
     * 浏览数
     */
    @TableField("view")
    @Builder.Default
    private Integer view = 0;

    /**
     * 是否还在审核
     */
    @Builder.Default
    @TableField("is_audit")
    private Boolean isAudit = true;

    /**
     * 是否已满员
     */
    @Builder.Default
    @TableField("is_full")
    private Boolean isFull = false;

    /**
     * 是否已结束
     */
    @Builder.Default
    @TableField("is_over")
    private Boolean isOver = false;

    /**
     * 任务附件
     */
    @TableField(exist = false)
    private List<String> files;
}
