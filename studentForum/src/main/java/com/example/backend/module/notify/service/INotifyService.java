package com.example.backend.module.notify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.notify.entity.Notify;

import java.util.List;


public interface INotifyService extends IService<Notify> {
    /**
     * 根据用户找到通知内容
     * @param userId
     * @return
     */
    List<Notify> getNotifyByUserId(String userId);


    List<Notify> getPraiseNotifyByUserId(String userId);
    List<Notify> getCommentNotifyByUserId(String userId);
    List<Notify> getCollectNotifyByUserId(String userId);
    List<Notify> getReplyNotifyByUserId(String userId);
    List<Notify> getFollowNotifyByUserId(String userId);



    /**
     * 创建通知
     * @author hwk
     * @param userId 被通知者id
     * @param fromId 通知产生者id
     * @param action 通知类型 1.点赞帖子 2.评论 3.收藏 4，回复 5.关注
     * @param topicId 只有关注没有topicId （= NULL）
     * @return Notify
     */
    Notify createNotify(String userId, String fromId, String action, String topicId);

    Integer countNotify(String userId, String action);
}
