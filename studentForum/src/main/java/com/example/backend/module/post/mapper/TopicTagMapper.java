package com.example.backend.module.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.module.post.entity.TopicTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

// 方法定义在 TopicTagMapper.xml 中
@Repository
public interface TopicTagMapper extends BaseMapper<TopicTag> {
    /**
     * 根据标签获取话题ID集合
     *
     * @param id
     * @return
     */
    Set<String> getTopicIdsByTagId(@Param("id") String id);
}

