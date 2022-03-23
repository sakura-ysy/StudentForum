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

    // 通知者Id
    @TableField("notifier")
    private String notifier;

    // 被通知者Id
    @TableField("receiver")
    private String receiver;

    // 通知来源
    @TableField("target_id")
    private String targetId;

    // 通知类型
    @TableField("type")
    private int type;

    // 是否未读
    @Builder.Default
    @TableField("status")
    private int status = 0;

    // 创建时间
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}