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
@TableName("exception_log")
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionLog implements Serializable {
    private static final long serialVersionUID = 1940884269715084223L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("requ_param")
    private String ExcRequParam;  // 请求Param

    @TableField("name")
    private String ExcName;  // 异常名

    @TableField("message")
    private String ExcMessage;  // 异常信息

    @TableField("user_id")
    private String ExcUserId;  // 操作员ID

    @TableField("user_name")
    private String ExcUserName;  // 操作员用户名

    @TableField("method")
    private String ExcMethod;  // 操作方法

    @TableField("url")
    private String ExcUrl;  // url

    @TableField("ip")
    private String ExcIp;  // ip

    @TableField("create_time")
    private Date OperCreateTime;  // 异常发生时间

    @TableField(exist = false)
    private String timeString;  // String型 creat_time
}
