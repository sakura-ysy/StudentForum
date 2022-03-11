package com.example.backend.module.certification.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@TableName("certification")
@AllArgsConstructor
@NoArgsConstructor
public class Certification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;


    /**
     * 成就
     */
    @NotBlank(message = "内容不可以为空")
    @TableField(value = "achievement")
    private String achievement;

    /*
    * 关键词
    */
    @TableField(value = "key_word")
    private String keyWord;

    /**
     * 标签
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 内容
     */
    @NotBlank(message = "内容不可以为空")
    @TableField("`content`")
    private String content;

    /**
     * 上传文件
     */
    @TableField(value = "files")
    private String files;

    /*
    * 赞成
    * */
    @TableField(value = "agree")
    @Builder.Default
    private Integer agree = 0;

    /*
    * 反对
    * */
    @TableField(value = "disagree")
    @Builder.Default
    private Integer disagree = 0;

    /*
     * 限制验证人数量
     * */
    @TableField(value = "num_limit")
    private Integer numLimit;

    /**
    * 已经验证的人的人数
    */
    @TableField(value = "num_sum")
    @Builder.Default
    private Integer numSum = 0;

    /**
     * 是否通过
     */
    @TableField("is_pass")
    private Boolean isPass;

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

