package com.example.backend.module.post.entity;

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
@TableName("bms_comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 作者ID
     */
    @TableField("user_id")
    private String userId;  // 评论者id

    /**
     * topicID
     */
    @TableField("topic_id")
    private String topicId;   // 文章id

    /**
     * 设计有二级评论
     * 父级评论id，也就是一级评论，一级评论id默认为NULL
     */

    @TableField("parent_comment_id")
    private String parentCommentId;

    /**
     * 一级评论只可能回复的是帖子本身，为NULL
     * 二级评论是在一级评论区展开讨论，回复的用户可能不同
     */
    @TableField("replyTo_id")
    private String replyToId;

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

    /**
     * 是否已审核
     * 是否审核通过
     */
    @TableField("is_audited")
    private Boolean isAudited;

    /**
     * 是否审核通过
     */
    @TableField("is_pass")
    private Boolean isPass;
}
