package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.entity.PostPraise;
import com.example.backend.module.post.mapper.PostPraiseMapper;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.post.service.IPostPraiseService;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.io.IOException;

import static com.example.backend.common.api.ApiErrorCode.POST_NOT_EXISTS;

@Service
public class IPostPraiseServiceImpl extends ServiceImpl<PostPraiseMapper, PostPraise> implements IPostPraiseService {
    @Resource
    private IPostPraiseService iPostPraiseService;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 点赞
     * @param postId
     * @param userName
     * @return
     */
    @Override
    public Integer executePraise(String postId, String userName) throws IOException {
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post)){
            ErrorResponse.sendJsonMessage(POST_NOT_EXISTS);
            return -1;   // 帖子不存在
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        PostPraise selectOne = baseMapper.selectOne(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getUserId, user.getId()).eq(PostPraise::getPostId, postId));
        if (!ObjectUtils.isEmpty(selectOne))
            return 0;     // 已点赞
        PostPraise addPostPraise = PostPraise.builder()
                .userId(user.getId())
                .postId(postId)
                .build();
        baseMapper.insert(addPostPraise);

        post.setPraises(post.getPraises() + 1);
        topicMapper.updateById(post);
        return 1;     // 成功
    }

    /**
     * 取消点赞
     * @param postId
     * @param userName
     * @return
     */
    @Override
    public Integer executeUnPraise(String postId, String userName) throws IOException {
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post)){
            ErrorResponse.sendJsonMessage(POST_NOT_EXISTS);
            return -1;   // 帖子不存在
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userName));
        PostPraise selectOne = baseMapper.selectOne(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getUserId, user.getId()).eq(PostPraise::getPostId, postId));
        if (ObjectUtils.isEmpty(selectOne))
            return 0;   // 还未点赞
        iPostPraiseService.remove(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId, postId)
                .eq(PostPraise::getUserId, user.getId()));
        post.setPraises(post.getPraises() + 1);
        topicMapper.updateById(post);
        return 1;  // 成功
    }
}
