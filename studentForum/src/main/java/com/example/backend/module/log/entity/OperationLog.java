package com.example.backend.module.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@TableName("oper_log")
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog implements Serializable {
    private static final long serialVersionUID = -8676998795338603239L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("module")
    private String OperModule;  // 操作模块

    @TableField("type")
    private String OperType;  // 操作类型

    @TableField("des")
    private String OperDesc;  // 操作描述

    @TableField("method")
    private String OperMethod;  // 操作方法

    @TableField("ip")
    private String OperIp;  // 请求Ip

    @TableField("requ_param")
    private String OperRequParam;  // 请求参数

    @TableField("resp_param")
    private String OperRespParam;  // 返回结果

    @TableField("user_id")
    private String OperUserId;  // 请求用户ID

    @TableField("user_name")
    private String OperUserName;  // 请求用户名

    @TableField("url")
    private String OperUrl;  // 请求Url

    @TableField("create_time")
    private Date OperCreateTime;  // 请求时间

    @TableField("ver")
    private String OperVer;  // 请求版本
}