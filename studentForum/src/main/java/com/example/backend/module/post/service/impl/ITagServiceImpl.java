package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.entity.Tag;
import com.example.backend.module.post.mapper.TagMapper;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.post.service.ITagService;
import com.example.backend.module.post.service.ITopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Tag 实现类
 *
 * @author Knox 2020/11/7
 */
@Service
public class ITagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private ITopicTagService ITopicTagService;

    @Autowired
    private IPostService IPostService;

    // 发布帖子时的插入标签功能
    @Override
    public List<Tag> insertTags(List<String> tagNames) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            // 判断该标签是否已存在
            Tag tag = this.baseMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagName));
            if (tag == null) {
                // 不存在，则创建一个并插入数据库
                tag = Tag.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                // 存在，将该标签下的帖子数加一
                tag.setTopicCount(tag.getTopicCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }

    @Override
    public Page<Post> selectTopicsByTagId(Page<Post> topicPage, String id) {

        // 获取关联的话题ID
        Set<String> ids = ITopicTagService.selectTopicIdsByTagId(id);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Post::getId, ids);

        return IPostService.page(topicPage, wrapper);
    }

}