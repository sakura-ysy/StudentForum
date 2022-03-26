package com.example.backend.module.question.vo;

import com.example.backend.module.post.entity.Tag;
import com.example.backend.module.question.entity.QTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionVO implements Serializable {
    private static final long serialVersionUID = 5838704114178542560L;

    /**
     * 问题ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 赏金
     */
    private Integer reward;

    /**
     * 问题关联标签
     */
    private List<QTag> tags;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 发布者用户名
     */
    private String username;

    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 回答数
     */
    private Integer ansNum;

    /**
     * 是否已经解决
     */
    private Boolean isSolved;

    /**
     * 是否已撤销
     */
    private Boolean isCanceled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否审核通过
     */
    private Boolean isPass;
}
