package com.example.backend.module.post.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVO {

    /**
     * 评论id
     */
    private String id;

    /**
     * 评论内容跟
     */
    private String content;

    /**
     * 所处帖子ID
     */
    private String topicId;

    /**
     * 发布者用户id
     */
    private String userId;

    /**
     * 发布者用户名
     */
    private String username;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 回复的评论id
     */
    private String replyToId = null;

    /**
     * 子评论
     */
    private List<CommentVO> childComment = null;
}

