package com.example.backend.module.teacher.entity;

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

@ApiModel("教师实体")
@Data
@Builder
@TableName("teacher")
@AllArgsConstructor
@NoArgsConstructor
public class Teacher implements Serializable {

    private static final long serialVersionUID = 7173300135641561743L;

    @ApiModelProperty("教师id")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private String userId;

    @ApiModelProperty("用户id")
    @TableField("username")
    private String userName;

    @ApiModelProperty("所在学校")
    @TableField("school")
    private String school;

    @ApiModelProperty("学位")
    @TableField("academy")
    private String academy;

    @ApiModelProperty("研究方向")
    @TableField("direction")
    private String direction;

    @ApiModelProperty("工号")
    @TableField("work_number")
    private String workNum;

    @ApiModelProperty("职称")
    @TableField("position")
    private String position;
}
