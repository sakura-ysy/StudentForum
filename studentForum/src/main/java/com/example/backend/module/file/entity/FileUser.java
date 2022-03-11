package com.example.backend.module.file.entity;

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
@TableName("file_user")
@AllArgsConstructor
@NoArgsConstructor
public class FileUser implements Serializable {

    private static final long serialVersionUID = 6517261664806090567L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件url
     */
    @TableField("file_url")
    private String url;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

}
