package com.example.backend.module.user.entity;

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
@TableName("permission")
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {
    private static final long serialVersionUID = -9025232978054468082L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("perm_name")
    private String permName;

    @TableField("perm_tag")
    private String permTag;

    @TableField("url")
    private String url;
}
