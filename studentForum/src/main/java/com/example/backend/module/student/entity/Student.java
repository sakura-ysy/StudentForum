package com.example.backend.module.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("学生实体")
@Data
@Builder
@TableName("student")
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {

    private static final long serialVersionUID = -7961424403456841255L;

    @ApiModelProperty("学生id")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty("用户名")
    @TableField("username")
    private String userName;

    @ApiModelProperty("所在学校")
    @TableField("school")
    private String school;

    @ApiModelProperty("学位")
    @TableField("academy")
    private String academy;

    @ApiModelProperty("专业")
    @TableField("major")
    private String major;

    @ApiModelProperty("学号")
    @TableField("student_number")
    private String stuNum;

    @ApiModelProperty("年级")
    @TableField("grade")
    private String grade;

}
