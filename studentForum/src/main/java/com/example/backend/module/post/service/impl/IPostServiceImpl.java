package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.post.dto.CreateTopicDTO;
import com.example.backend.module.post.entity.*;
import com.example.backend.module.post.mapper.*;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.post.service.ITagService;
import com.example.backend.module.post.service.ITopicTagService;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.UserMapper;
import com.example.backend.module.user.service.IUserService;
import com.example.backend.module.user.vo.ProfileVO;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    @Lazy
    private ITagService iTagService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ITopicTagService ITopicTagService;

    // 获取分页下的文章信息
    @Override
    public Page<PostVO> getList(Page<PostVO> page, String tab) {
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.selectListAndPage(page, tab);
        // 查询话题的标签
        setTopicTags(iPage);
        // 获取点赞、收藏、评论数
        setReactionNum(iPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Post create(CreateTopicDTO dto, User user) {
        // 查询帖子标题是否已存在
        Post topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getTitle, dto.getTitle()));
        Assert.isNull(topic1, "话题已存在，请修改");

        // 创建帖子对象，封装
        Post topic = Post.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .build();
        String tags = "";
        for (String tag : dto.getTags()) {
            tags = tags + tag + ";";
        }
        topic.setTags(tags);
        this.baseMapper.insert(topic);  // 将该对象插入表单
        
        return topic;
    }

    // 返回指定id的帖子
    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        Post topic = this.baseMapper.selectById(id);  // topic 为指定的帖子对象
        Assert.notNull(topic, "当前话题不存在,或已被作者删除");
        // 查询话题详情
        topic.setView(topic.getView() + 1);  // view表示帖子的访问次数，访问一次加一一次
        this.baseMapper.updateById(topic);
        // emoji转码
        topic.setContent(EmojiParser.parseToUnicode(topic.getContent()));
        // 点赞、收藏、评论数
        List<PostCollect> collectList = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,topic.getId()));
        topic.setCollects(collectList.size());
        List<PostPraise> praiseList = postPraiseMapper.selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,topic.getId()));
        if (ObjectUtils.isEmpty(praiseList))
            topic.setPraises(0);
        else
            topic.setPraises(praiseList.size());
        List<CommentVO> commentVOList = commentMapper.getCommentsByTopicID(topic.getId());
        topic.setComments(commentVOList.size());
        map.put("topic", topic);
        // 标签  找到帖子绑定的所有标签
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topic.getId());
        Set<String> set = new HashSet<>();
        for (TopicTag articleTag : ITopicTagService.list(wrapper)) {
            set.add(articleTag.getTagId());  // 把每一个相关标签的id存放在set中
        }
        List<Tag> tags = iTagService.listByIds(set);  // 通过set得到每一个标签的具体信息，存入集合
        map.put("tags", tags);

        // 作者

        ProfileVO user = iUserService.getUserProfile(topic.getUserId()); // 得到用户信息，只需要Profile中的字段
        map.put("user", user);

        return map;
    }

    @Override
    public List<Post> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }

    // 查询帖子
    @Override
    public Page<PostVO> searchByKey(String keyword, Page<PostVO> page) {
        // 查询话题
        Page<PostVO> iPage = this.baseMapper.searchByKey(page, keyword);
        // 查询话题的标签
        setTopicTags(iPage);
        return iPage;
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
    private void setReactionNum(Page<PostVO> iPage) {
        iPage.getRecords().forEach(topic -> {
            List<TopicTag> topicTags = ITopicTagService.selectByTopicId(topic.getId());
            if (!topicTags.isEmpty()) {
                List<PostCollect> collectList = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId,topic.getId()));
                topic.setCollects(collectList.size());
                List<PostPraise> praiseList = postPraiseMapper.selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId,topic.getId()));
                if (ObjectUtils.isEmpty(praiseList))
                    topic.setPraises(0);
                else
                    topic.setPraises(praiseList.size());
                List<CommentVO> commentVOList = commentMapper.getCommentsByTopicID(topic.getId());
                topic.setComments(commentVOList.size());
            }
        });
    }
}