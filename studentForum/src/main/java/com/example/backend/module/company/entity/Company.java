package com.example.backend.module.company.entity;

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
@TableName("company")
@AllArgsConstructor
@NoArgsConstructor
public class Company implements Serializable {

    private static final long serialVersionUID = -996154405328338872L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("username")
    private String userName;

    @TableField("company_name")
    private String comName;

}
