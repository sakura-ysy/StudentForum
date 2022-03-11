package com.example.backend.module.notify.entity;

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
@TableName("notify")
@AllArgsConstructor
@NoArgsConstructor
public class Notify implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    // 内容
    @NotBlank(message = "内容不可以为空")
    @TableField(value = "content")
    private String content;

     // 被通知者Id
    @TableField("user_id")
    private String userId;

    // 通知来源
    @TableField("from_id")
    private String fromId;

    // 通知类型
    // 1.点赞 2.评论 3.收藏 4，回复 5.关注
    @TableField("action")
    private String action;

    // 帖子号
    @TableField("topic_id")
    private String topicId;

    // 是否未读
    @Builder.Default
    @TableField("`is_Read`")
    private int isRead = 0;

    // 创建时间
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
