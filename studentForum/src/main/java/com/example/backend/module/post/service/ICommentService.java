package com.example.backend.module.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.dto.CommentDTO;
import com.example.backend.module.post.entity.Comment;
import com.example.backend.module.post.vo.CommentVO;
import com.example.backend.module.user.entity.User;

import java.io.IOException;
import java.util.List;

public interface ICommentService extends IService<Comment> {

    /**
     * 获取全部评论
     * @param postId
     * @return
     */
    List<CommentVO> getCommentTreeByPostId(String postId) throws IOException;

    /**
     * 获取一级评论
     * @param topicid
     * @return {@link Comment}
     */
    List<CommentVO> getFirstLevelCommentsByTopicID(String topicid) throws IOException;

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