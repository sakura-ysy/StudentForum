package com.example.backend.module.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.post.entity.PostPraise;

import java.io.IOException;

public interface IPostPraiseService extends IService<PostPraise> {
    /**
     * 点赞
     * @param postId
     * @param userName
     * @return
     */
    Integer executePraise(String postId, String userName) throws IOException;

    /**
     * 取消点赞
     * @param postId
     * @param userName
     * @return
     */
    Integer executeUnPraise(String postId, String userName) throws IOException;
}
