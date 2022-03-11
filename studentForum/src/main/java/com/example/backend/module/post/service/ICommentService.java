package com.example.backend.module.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.dto.CommentDTO;
import com.example.backend.module.post.entity.Comment;
import com.example.backend.module.post.entity.CommentVO;
import com.example.backend.module.user.entity.User;

import java.util.List;

public interface ICommentService extends IService<Comment> {
    /**
     * 获取一级评论
     * @param topicid
     * @return {@link Comment}
     */
    List<CommentVO> getFirstLevelCommentsByTopicID(String topicid);

    /**
     * 发起一级评论
     * @param dto
     * @param principal
     * @return
     */
    Comment createFirstLevelComment(CommentDTO dto, User principal);

    /**
     * 发起二级评论
     * @param dto
     * @param principal
     * @return
     */
    Comment createSecondLevelComment(CommentDTO dto, User principal);

    /**
     * 获取二级评论
     * @param topicId
     * @param parentId
     * @return
     */
    List<CommentVO> getSecondLevelCommentsByParentID(String topicId, String parentId);
}