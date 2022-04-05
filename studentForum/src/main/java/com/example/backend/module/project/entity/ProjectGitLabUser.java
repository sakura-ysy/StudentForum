package com.example.backend.module.project.entity;


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
public class ProjectGitLabUser {
    private static final long serialVersionUID = -1660236269782860229L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("gitlab_username")
    private String gitLabUsername;

    @TableField("project_id")
    private String projectId;

    @TableField("project_name")
    private String projectName;

    @TableField("project_role")
    private int projectRole;
}
