package com.example.backend.module.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
@TableName("project")
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private static final long serialVersionUID = 4922663593069261192L;
    /**
     * 主键
     */
    @NotBlank(message = "项目id不可以空")
    @TableId(value = "id")
    private String id;
    /**
     * 项目名
     */
    @NotBlank(message = "项目名不可以为空")
    @TableField(value = "name")
    private String name;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @TableField(value = "description")
    private String description;

    /**
     * star
     */
    @Builder.Default
    @TableField("stars")
    private Integer stars = 0;
    /**
     * forks
     */
    @Builder.Default
    @TableField(value = "forks")
    private Integer forks = 0;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    @TableField(value = "latest_message")
    private String latestMessage;

    /**
     * 是否已审核
     */
    @TableField("is_audited")
    @Builder.Default
    private Boolean isAudited = true;

    /**
     * 是否审核通过
     */
    @TableField("is_pass")
    @Builder.Default
    private Boolean isPass = true;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time", fill = FieldFill.UPDATE)
    private Date modifyTime;
}
