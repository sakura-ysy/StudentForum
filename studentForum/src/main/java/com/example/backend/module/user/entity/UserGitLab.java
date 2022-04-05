package com.example.backend.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@TableName("user_gitlabuser")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserGitLab {
    private static final long serialVersionUID = -1660236269782860229L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("user_name")
    private String username;

    @TableField("gitlab_username")
    private String gitLabUsername;
}
