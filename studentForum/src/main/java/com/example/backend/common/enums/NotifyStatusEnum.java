package com.example.backend.common.enums;

public enum NotifyStatusEnum {
    UNREAD(0),
    READ(1)
    ;
    private final int status;

    NotifyStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
