package com.example.backend.module.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@TableName("post_praise")
@Accessors(chain = true)
public class PostPraise implements Serializable {
    private static final long serialVersionUID = 4914024134108800529L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 帖子id
     */
    @TableField("post_id")
    private String postId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;
}
