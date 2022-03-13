package com.example.backend.module.post.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.dto.CreateTopicDTO;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.user.entity.User;

import java.util.List;
import java.util.Map;

public interface IPostService extends IService<Post> {

    /**
     * 获取首页话题列表
     *
     * @param page
     * @param tab
     * @return
     */
    Page<PostVO> getList(Page<PostVO> page, String tab);
    /**
     * 发布
     *
     * @param dto
     * @param principal
     * @return
     */
    Post create(CreateTopicDTO dto, User principal);

    /**
     * 查看话题详情
     *
     * @param id
     * @return
     */
    Map<String, Object> viewTopic(String id);
    /**
     * 获取随机推荐10篇
     *
     * @param id
     * @return
     */
    List<Post> getRecommend(String id);
    /**
     * 关键字检索
     *
     * @param keyword
     * @param page
     * @return
     */
    Page<PostVO> searchByKey(String keyword, Page<PostVO> page);

    /**
     * 按标签搜索
     * @param tag
     * @param page
     * @return
     */
    Page<PostVO> searchByTag(String tag, Page<PostVO> page);
}
