package com.example.backend.module.certification.entity;

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
@TableName("certification_vote")
@AllArgsConstructor
@NoArgsConstructor
public class CertificationVote implements Serializable{
    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 投票人id
     */
    @TableField("voter_id")
    private String voterId;

    /**
     * 投票的认证的id
     */
    @TableField("certification_id")
    private String certificationId;

    /**
     * 用户id
     */
    @TableField("is_agree")
    private boolean isAgree;
}
