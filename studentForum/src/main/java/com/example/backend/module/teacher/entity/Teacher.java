package com.example.backend.module.teacher.entity;

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
@TableName("teacher")
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable {

    private static final long serialVersionUID = 7173300135641561743L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("username")
    private String userName;

    @TableField("school")
    private String school;

    @TableField("academy")
    private String academy;

    @TableField("direction")
    private String direction;

    @TableField("work_number")
    private String workNum;

    @TableField("position")
    private String position;
}
