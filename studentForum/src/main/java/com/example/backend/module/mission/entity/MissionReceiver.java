package com.example.backend.module.mission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@TableName("mission_receiver")
@AllArgsConstructor
@NoArgsConstructor
public class MissionReceiver implements Serializable {
    private static final long serialVersionUID = 1778117747792013611L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 任务id
     */
    @TableField("mission_id")
    private String missionId;

    /**
     * 接收者id
     */
    @TableField("receiver_id")
    private String receiverId;

    /**
     * 接收者用户名
     */
    @TableField("receiver_name")
    private String receiverName;
    /**
     * 是否已完成
     */
    @Builder.Default
    @TableField("is_finish")
    private Boolean isFinish = false;
}
