package com.example.backend.module.student.entity;

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
@TableName("student")
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {

    private static final long serialVersionUID = -7961424403456841255L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("username")
    private String userName;

    @TableField("school")
    private String school;

    @TableField("academy")
    private String academy;

    @TableField("major")
    private String major;

    @TableField("student_number")
    private String stuNum;

    @TableField("grade")
    private String grade;

}
