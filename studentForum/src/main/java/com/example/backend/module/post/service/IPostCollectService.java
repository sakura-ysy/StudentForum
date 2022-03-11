package com.example.backend.module.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.entity.PostCollect;

public interface IPostCollectService extends IService<PostCollect> {
    /**
     * 收藏
     * @param postId
     * @param userName
     * @return
     */
    Integer executeCollect(String postId, String userName);

    /**
     * 取消收藏
     * @param postId
     * @param userName
     * @return
     */
    Integer executeUnCollect(String postId, String userName);
}
