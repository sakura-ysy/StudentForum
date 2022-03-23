package com.example.backend.common.enums;

public enum NotifyTypeEnum {
    ALL_NOTIFY(0, "所有帖子"),
    POST_PRAISE(101, "点赞了你的帖子"),
    POST_COLLECT(102, "收藏了你的帖子"),
    POST_COMMENT(103, "评论了你的帖子"),
    COMMENT_COMMENT(201, "评论了你的评论"),
    USER_SUBSCRIBE(301, "关注了你")
    ;

    public int getTargetType() {
        return targetType;
    }

    public String getName() {
        return name;
    }

    private final int targetType;
    private final String name;
    NotifyTypeEnum(int targetType, String name) {
        this.targetType = targetType;
        this.name = name;
    }
    public static NotifyTypeEnum getEnumByType(int type) {
        for (NotifyTypeEnum notifyType : NotifyTypeEnum.values()) {
            if (notifyType.getTargetType() == type) {
                return notifyType;
            }
        }
        return null;
    }
}
