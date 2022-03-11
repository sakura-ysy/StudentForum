package com.example.backend.module.notify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.mapper.NotifyMapper;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.user.service.IUserService;
import com.example.backend.module.user.vo.ProfileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class INotifyServiceImpl extends ServiceImpl<NotifyMapper, Notify> implements INotifyService {

    @Resource
    private IUserService iUserService;
    @Resource
    private TopicMapper topicMapper;

    @Override
    public List<Notify> getNotifyByUserId(String userId) {
        List<Notify> notifies = new ArrayList<Notify>();
        try {
            notifies = this.baseMapper.getNotifyByUserId(userId);
        } catch (Exception e) {
            log.info("获取用户通知失败");
            e.printStackTrace();
        }
        return notifies;
    }

    @Override
    public List<Notify> getPraiseNotifyByUserId(String userId) {
        List<Notify> notifies = new ArrayList<Notify>();
        try {
            notifies = this.baseMapper.getPraiseNotifyByUserId(userId);
        } catch (Exception e) {
            log.info("获取用户所有通知失败");
            e.printStackTrace();
        }
        return notifies;
    }

    @Override
    public List<Notify> getCommentNotifyByUserId(String userId) {
        List<Notify> notifies = new ArrayList<Notify>();
        try {
            notifies = this.baseMapper.getCommentNotifyByUserId(userId);
        } catch (Exception e) {
            log.info("获取用户评论点赞通知失败");
            e.printStackTrace();
        }
        return notifies;
    }

    @Override
    public List<Notify> getCollectNotifyByUserId(String userId) {
        List<Notify> notifies = new ArrayList<Notify>();
        try {
            notifies = this.baseMapper.getCollectNotifyByUserId(userId);
        } catch (Exception e) {
            log.info("获取收藏用户通知失败");
            e.printStackTrace();
        }
        return notifies;
    }

    @Override
    public List<Notify> getReplyNotifyByUserId(String userId) {
        List<Notify> notifies = new ArrayList<Notify>();
        try {
            notifies = this.baseMapper.getReplyNotifyByUserId(userId);
        } catch (Exception e) {
            log.info("获取用户回复通知失败");
            e.printStackTrace();
        }
        return notifies;
    }

    @Override
    public List<Notify> getFollowNotifyByUserId(String userId) {
        List<Notify> notifies = new ArrayList<Notify>();
        try {
            notifies = this.baseMapper.getFollowNotifyByUserId(userId);
        } catch (Exception e) {
            log.info("获取用户关注通知失败");
            e.printStackTrace();
        }
        return notifies;
    }

    /**
     * @author hwk
     * @param userId 被通知者id
     * @param fromId 通知产生者id
     * @param action 通知类型 1.点赞2.评论 3.收藏 4，回复 5.关注
     * @param topicId 只有关注没有topicId （= NULL）
     * @return Notify
     */
    @Override
    public Notify createNotify(String userId, String fromId, String action, String topicId) {
        String content = "";
        ProfileVO userProfile = iUserService.getUserProfile(fromId);
        String title = "";
        if(topicId != null) {
            Post post = topicMapper.selectById(topicId);
            title = post.getTitle();
        }
        String username = userProfile.getUsername();

        switch (action) {
            case "1":
                content = username + "点赞了你的帖子: " + title + "!";
                break;
            case "2":
                content = username + "评论了你的帖子: " + title + "!";
                break;
            case "3":
                content = username + "收藏了你的帖子: " + title + "!";
                break;
            case "4":
                content = username + "回复了你在帖子:" + title + " 下的的评论!";
                break;
            case "5":
                content = username + "关注了你!";
                break;
            default:
                log.info("创建通知失败，操作类型有误");
                return null;
        }
        Notify notify = Notify.builder()
                .userId(userId)
                .fromId(fromId)
                .action(action)
                .content(content)
                .createTime(new Date())
                .topicId(topicId).build();
        this.baseMapper.insert(notify);
        return notify;
    }

    @Override
    public Integer countNotify(String userId, String action) {
        return this.baseMapper.selectCount(new QueryWrapper<Notify>().eq("action", action).eq("is_Read","0"));
    }
}
