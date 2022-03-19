package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.post.dto.CommentDTO;
import com.example.backend.module.post.entity.Comment;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.post.vo.CommentVO;
import com.example.backend.module.post.mapper.CommentMapper;
import com.example.backend.module.post.service.ICommentService;
import com.example.backend.module.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.backend.common.api.ApiErrorCode.COMMENT_FAILED;

@Slf4j
@Service
public class ICommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private IPostService iPostService;

    /**
     * 获取帖子评论树
     * @param postId
     * @return
     * @throws IOException
     */
    @Override
    public List<CommentVO> getCommentTreeByPostId(String postId) throws IOException {
        // 先获取帖子的所有一级评论
        List<CommentVO> list = this.getFirstLevelCommentsByTopicID(postId);
        // 获取一级评论的所有二级评论
        for (CommentVO commentVO : list) {
            String comId = commentVO.getId();
            commentVO.setChildComment(this.getSecondLevelCommentsByParentID(postId,comId));
        }
        return list;
    }

    /**
     * 获取帖子所有一级评论
     * @param topicid
     * @return
     * @throws IOException
     */
    @Override
    public List<CommentVO> getFirstLevelCommentsByTopicID(String topicid) throws IOException {
        List<CommentVO> lstBmsComment = new ArrayList<CommentVO>();
        lstBmsComment = this.baseMapper.getCommentsByTopicID(topicid);
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
        // 帖子评论数加1
        Post post = iPostService.getById(dto.getPostId());
        post.setComments(post.getComments() + 1);
        iPostService.updateById(post);
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
        // 帖子评论数加1
        Post post = iPostService.getById(dto.getPostId());
        post.setComments(post.getComments() + 1);
        iPostService.updateById(post);
        return comment;
    }

    /**
     * 获取一级评论的所有二级评论
     * @param topicId
     * @param parentId
     * @return
     */
    @Override
    public List<CommentVO> getSecondLevelCommentsByParentID(String topicId, String parentId) {
        List<CommentVO> SecondLevelComments = new ArrayList<CommentVO>();
        // 通过parentId获取二级评论
        SecondLevelComments = this.baseMapper.getSecondLevelCommentsByParentID(topicId, parentId);
        return SecondLevelComments;
    }
}
