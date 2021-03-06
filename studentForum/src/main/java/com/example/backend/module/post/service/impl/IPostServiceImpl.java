package com.example.backend.module.post.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.post.dto.CreateTopicDTO;
import com.example.backend.module.post.entity.*;
import com.example.backend.module.post.mapper.*;
import com.example.backend.module.post.service.*;
import com.example.backend.module.post.vo.CommentVO;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.UserMapper;
import com.example.backend.module.user.service.IUserService;
import com.example.backend.module.user.vo.ProfileVO;
import com.vdurmont.emoji.EmojiParser;
import javafx.geometry.Pos;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.backend.common.api.ApiErrorCode.POST_NOT_EXISTS;
import static com.example.backend.common.api.ApiErrorCode.USER_NOT_EXISTS;

@Service
public class IPostServiceImpl extends ServiceImpl<TopicMapper, Post> implements IPostService {
    @Resource
    private TagMapper tagMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PostPraiseMapper postPraiseMapper;
    @Resource
    private PostCollectMapper postCollectMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private IPostPraiseService iPostPraiseService;
    @Resource
    private IPostCollectService iPostCollectService;

    @Autowired
    @Lazy
    private ITagService iTagService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ITopicTagService ITopicTagService;

    // ??????????????????????????????
    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        // ????????????
        Page<PostVO> iPage = this.baseMapper.selectListAndPage(page, tab);
        Page<PostVO> res = new Page<PostVO>(page.getCurrent(),page.getSize());
        res.setTotal(iPage.getTotal());
        List<PostVO> list = new ArrayList<>();
        for (PostVO postVO : iPage.getRecords()) {
            // ??????, ??????, ?????????
            List<PostPraise> praiseList = iPostPraiseService.getBaseMapper().selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,postVO.getId()));
            postVO.setPraises(praiseList.size());
            List<PostCollect> collectList = iPostCollectService.getBaseMapper().selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,postVO.getId()));
            postVO.setCollects(collectList.size());
            List<Comment> commentList = commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getTopicId,postVO.getId()));
            postVO.setComments(commentList.size());
            // tag??????
            List<Tag> tags = new ArrayList<>();
            // ??????
            List<TopicTag> postTag = ITopicTagService.getBaseMapper().selectList(new LambdaQueryWrapper<TopicTag>().eq(TopicTag::getTopicId,postVO.getId()));
            for (TopicTag topicTag : postTag) {
                Tag tag = iTagService.getById(topicTag.getTagId());
                tags.add(tag);
            }
            postVO.setTags(tags);
            list.add(postVO);
        }
        res.setRecords(list);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostVO create(CreateTopicDTO dto, User user) {
        // ???????????????????????????
        Post post = Post.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .build();
        this.baseMapper.insert(post);  // ????????????????????????
        post = this.getBaseMapper().selectOne(new LambdaQueryWrapper<Post>().eq(Post::getId, post.getId()));

        // ????????????
        for (String tag : dto.getTags()) {
            Tag existTag = iTagService.getBaseMapper().selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName,tag));
            if (existTag == null){
                Tag newTag = Tag.builder().name(tag).topicCount(1).build();
                iTagService.getBaseMapper().insert(newTag);
            }
            else{
                existTag.setTopicCount(existTag.getTopicCount()+1);
                iTagService.getBaseMapper().updateById(existTag);
            }
            TopicTag postTag = TopicTag.builder()
                    .tagId(iTagService.getBaseMapper().selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName,tag)).getId())
                    .topicId(post.getId())
                    .build();
            ITopicTagService.getBaseMapper().insert(postTag);
        }
        PostVO postVO = this.changePostToPostVO(post);
        return postVO;
    }

    // ????????????id?????????
    @Override
    public Map<String, Object> viewTopic(String id) throws IOException {
        Map<String, Object> map = new HashMap<>(16);
        Post topic = this.baseMapper.selectById(id);  // topic ????????????????????????
        if (topic == null){
            ErrorResponse.sendJsonMessage(POST_NOT_EXISTS);
            return null;
        }
        // ??????????????????
        topic.setView(topic.getView() + 1);  // view??????????????????????????????????????????????????????
        this.baseMapper.updateById(topic);
        // emoji??????
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        Map<String,Object> topicMap = JSONObject.parseObject(JSON.toJSONString(topic));

        // ????????????????????????
        List<PostPraise> praiseList = iPostPraiseService.getBaseMapper().selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,topic.getId()));
        topicMap.put("praises",praiseList.size());
        List<PostCollect> collectList = iPostCollectService.getBaseMapper().selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,topic.getId()));
        topicMap.put("collects",collectList.size());
        List<Comment> commentList = commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getTopicId,topic.getId()));
        topicMap.put("comments",commentList.size());

        map.put("topic", topicMap);
        // ??????  ?????????????????????????????????
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topic.getId());
        List<Tag> tags = new ArrayList<>();
        List<TopicTag>  topicTags = ITopicTagService.getBaseMapper().selectList(wrapper);
        if(topicTags != null){
            for (TopicTag topicTag : topicTags) {
                Tag tag = iTagService.getById(topicTag.getTagId());
                tags.add(tag);
            }
        }
        map.put("tags", tags);
        // ??????
        ProfileVO user = iUserService.getUserProfile(topic.getUserId()); // ??????????????????????????????Profile????????????
        map.put("user", user);
        return map;
    }

    @Override
    public List<Post> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }

    // ????????????
    @Override
    public Page<PostVO> searchByKey(String keyword, Page<PostVO> page) {
        // ????????????
        Page<PostVO> iPage = this.baseMapper.searchByKey(page, keyword);
        // ?????????????????????
        setTopicTags(iPage);
        return iPage;
    }
    /**
     * // ???????????????
     * @param tag
     * @param page
     * @return
     */
    @Override
    public Page<PostVO> searchByTag(String tag, Page<PostVO> page) {
        // TODO
        return null;
    }

    private void setTopicTags(Page<PostVO> iPage) {
        iPage.getRecords().forEach(topic -> {
            List<TopicTag> topicTags = ITopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<String> tagIds = topicTags.stream().map(TopicTag::getTagId).collect(Collectors.toList());
                List<Tag> tags = tagMapper.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
    }

    @Override
    public List<PostVO> getAllPostForUser(String userId) throws IOException {
        User user = iUserService.getById(userId);
        if(user == null){
            ErrorResponse.sendJsonMessage(USER_NOT_EXISTS);
            return null;
        }
        List<Post> posts = this.getBaseMapper().selectList(new LambdaQueryWrapper<Post>().eq(Post::getUserId,userId));
        List<PostVO> postVOs = new ArrayList<>();
        for (Post post : posts) {
            postVOs.add(this.changePostToPostVO(post));
        }
        return postVOs;
    }

    @Override
    public PostVO changePostToPostVO(Post post){
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post,postVO);
        // ??????, ??????, ?????????
        List<PostPraise> praiseList = iPostPraiseService.getBaseMapper().selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,post.getId()));
        postVO.setPraises(praiseList.size());
        List<PostCollect> collectList = iPostCollectService.getBaseMapper().selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,post.getId()));
        postVO.setCollects(collectList.size());
        List<Comment> commentList = commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getTopicId,post.getId()));
        postVO.setComments(commentList.size());

        List<Tag> tags = new ArrayList<>();
        // ??????
        List<TopicTag> postTag = ITopicTagService.getBaseMapper().selectList(new LambdaQueryWrapper<TopicTag>().eq(TopicTag::getTopicId,post.getId()));
        for (TopicTag topicTag : postTag) {
            Tag tag = iTagService.getById(topicTag.getTagId());
            tags.add(tag);
        }
        postVO.setTags(tags);
        // ????????????
        User user = iUserService.getById(post.getUserId());
        if (user != null){
            postVO.setAlias(user.getAlias());
            postVO.setAvatar(user.getAvatar());
            postVO.setUsername(user.getUsername());
        }
        return postVO;
    }
}
