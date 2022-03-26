package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.entity.PostCollect;
import com.example.backend.module.post.mapper.PostCollectMapper;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.post.service.IPostCollectService;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.io.IOException;

import static com.example.backend.common.api.ApiErrorCode.POST_NOT_EXISTS;

@Service
public class IPostCollectServiceImpl extends ServiceImpl<PostCollectMapper, PostCollect> implements IPostCollectService {
    @Resource
    private  IPostCollectService iPostCollectService;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 收藏
     * @param postId
     * @param userName
     * @return
     */
    @Override
    public Integer executeCollect(String postId, String userName) throws IOException {
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post)){
            ErrorResponse.sendJsonMessage(POST_NOT_EXISTS);
            return -1;
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        PostCollect selectOne = baseMapper.selectOne(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getUserId, user.getId()).eq(PostCollect::getPostId, postId));
        if (!ObjectUtils.isEmpty(selectOne))
            return 0;
        PostCollect addPostCollect = PostCollect.builder()
                .userId(user.getId())
                .postId(postId)
                .build();
        baseMapper.insert(addPostCollect);
        topicMapper.updateById(post);
        return 1;
    }

    /**
     * 取消收藏
     * @param postId
     * @param userName
     * @return
     */
    @Override
    public Integer executeUnCollect(String postId, String userName) throws IOException {
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post)){
            ErrorResponse.sendJsonMessage(POST_NOT_EXISTS);
            return -1;   // 帖子不存在
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        PostCollect selectOne = baseMapper.selectOne(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getUserId, user.getId()).eq(PostCollect::getPostId, postId));
        if (ObjectUtils.isEmpty(selectOne))
            return 0;   // 还未收藏
        iPostCollectService.remove(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId, postId)
                .eq(PostCollect::getUserId, user.getId()));
        topicMapper.updateById(post);
        return 1;  // 成功
    }
}
