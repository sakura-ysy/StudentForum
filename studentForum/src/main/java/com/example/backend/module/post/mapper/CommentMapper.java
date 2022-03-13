package com.example.backend.module.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.module.post.entity.Comment;
import com.example.backend.module.post.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// 方法定义在 CommentMapper.xml 文件中
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 一级评论
     * @param topicid
     * @return
     */
    List<CommentVO> getCommentsByTopicID(@Param("topicid") String topicid);

    /**
     * 二级评论
     * @param parentId
     * @return
     */
    List<CommentVO> getSecondLevelCommentsByParentID(@Param("topicId") String topicId, @Param("parentId") String parentId);
}
