package com.example.backend.module.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.entity.Tag;
import com.example.backend.module.post.entity.TopicTag;

import java.util.List;
import java.util.Set;

public interface ITopicTagService extends IService<TopicTag> {

    /**
     * 获取Topic Tag 关联记录
     *
     * @param topicId TopicId
     * @return
     */
    List<TopicTag> selectByTopicId(String topicId);
    /**
     * 创建中间关系
     *
     * @param id
     * @param tags
     * @return
     */
    void createTopicTag(String id, List<Tag> tags);
    /**
     * 获取标签换脸话题ID集合
     *
     * @param id
     * @return
     */
    Set<String> selectTopicIdsByTagId(String id);

}