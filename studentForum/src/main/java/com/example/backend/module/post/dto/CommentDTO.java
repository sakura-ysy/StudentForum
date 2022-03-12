package com.example.backend.module.post.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -5957433707110390852L;


    private String postId;

    /**
     * 内容
     */
    private String content;

    /**
     * 楼主评论（一级评论）id
     */
    private String parentId;

    /**
     * 被回复用户id
     */
    private String replyToId;

}
