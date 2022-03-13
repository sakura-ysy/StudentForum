package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.post.dto.CommentDTO;
import com.example.backend.module.post.entity.Comment;
import com.example.backend.module.post.vo.CommentVO;
import com.example.backend.module.post.mapper.CommentMapper;
import com.example.backend.module.post.service.ICommentService;
import com.example.backend.module.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ICommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    // 1. 获取指定帖子的所有评论
    @Override
    public List<CommentVO> getFirstLevelCommentsByTopicID(String topicid) {
        List<CommentVO> lstBmsComment = new ArrayList<CommentVO>();
        try {
            // 通过帖子id获取文章
            lstBmsComment = this.baseMapper.getCommentsByTopicID(topicid);
        } catch (Exception e) {
            log.info("获取一级评论失败");
        }
        return lstBmsComment;
    }

    // 2. 发起一级评论
    @Override
    public Comment createFirstLevelComment(CommentDTO dto, User user) {
        // 通过前端传入的 dto 来创建一个comment对象
        Comment comment = Comment.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .topicId(dto.getPostId())
                .createTime(new Date())
                .build();
        System.out.println(comment);
        // 插入表
        this.baseMapper.insert(comment);
        return comment;
    }

    // 3. 发起二级评论
    @Override
    public Comment createSecondLevelComment(CommentDTO dto, User user) {
        // 通过前端传入的 dto 来创建一个comment对象
        System.out.println(dto);
        Comment comment = Comment.builder()
                .userId(user.getId())
                .content(dto.getContent())
                .topicId(dto.getPostId())
                .createTime(new Date())
                .parentCommentId(dto.getParentId())
                .replyToId(dto.getReplyToId())
                .build();
        // 插入表
        this.baseMapper.insert(comment);
        return comment;
    }

    // 4.获得二级评论
    @Override
    public List<CommentVO> getSecondLevelCommentsByParentID(String topicId, String parentId) {
        List<CommentVO> SecondLevelComments = new ArrayList<CommentVO>();
        try {
            // 通过parentId获取二级评论
            SecondLevelComments = this.baseMapper.getSecondLevelCommentsByParentID(topicId, parentId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取二级评论失败");
        }
        return SecondLevelComments;
    }
}
