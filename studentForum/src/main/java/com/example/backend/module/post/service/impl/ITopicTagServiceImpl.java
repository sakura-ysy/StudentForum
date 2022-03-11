package com.example.backend.module.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.post.entity.Tag;
import com.example.backend.module.post.entity.TopicTag;
import com.example.backend.module.post.mapper.TopicTagMapper;
import com.example.backend.module.post.service.ITopicTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class ITopicTagServiceImpl extends ServiceImpl<TopicTagMapper, TopicTag> implements ITopicTagService {

    @Override
    public List<TopicTag> selectByTopicId(String topicId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topicId);
        return this.baseMapper.selectList(wrapper);
    }

    // 每当帖子编译绑定标签时，数据库中帖子和标签的对应关系就会变化，用如下方法实现
    @Override
    public void createTopicTag(String id, List<Tag> tags) {
        // 先删除topicId对应的所有记录
        this.baseMapper.delete(new LambdaQueryWrapper<TopicTag>().eq(TopicTag::getTopicId, id));

        // 循环保存对应关联
        tags.forEach(tag -> {
            TopicTag topicTag = new TopicTag();
            topicTag.setTopicId(id);
            topicTag.setTagId(tag.getId());
            this.baseMapper.insert(topicTag);
        });
    }
    @Override
    public Set<String> selectTopicIdsByTagId(String id) {
        return this.baseMapper.getTopicIdsByTagId(id);
    }

}
