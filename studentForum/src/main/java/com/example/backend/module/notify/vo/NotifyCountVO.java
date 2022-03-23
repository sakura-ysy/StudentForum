package com.example.backend.module.notify.vo;

import lombok.Data;

@Data
public class NotifyCountVO {
    // 定义参照NotifyTypeEnum
    private int allNotify;
    private int postPraise;
    private int postCollection;
    private int postComment;
    private int commentComment;
    private int userSubscribe;
}
