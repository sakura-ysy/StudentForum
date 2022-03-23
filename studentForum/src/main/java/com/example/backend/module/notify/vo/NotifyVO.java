package com.example.backend.module.notify.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NotifyVO {

    // 主键
    private String id;

    // 通知者Id
    private String notifier;

    // 通知标题
    private String title;

    // 通知来源
    private String targetId;

    // 是否未读
    private int status = 0;

    // 创建时间
    private Date createTime;
}
