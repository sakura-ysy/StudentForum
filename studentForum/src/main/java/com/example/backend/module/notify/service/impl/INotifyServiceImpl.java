package com.example.backend.module.notify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.common.enums.NotifyStatusEnum;
import com.example.backend.common.enums.NotifyTypeEnum;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.mapper.NotifyMapper;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.notify.vo.NotifyCountVO;
import com.example.backend.module.notify.vo.NotifyVO;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.backend.common.api.ApiErrorCode.USER_NOT_EXISTS;

@Slf4j
@Service
public class INotifyServiceImpl extends ServiceImpl<NotifyMapper, Notify> implements INotifyService {

    @Resource
    private IUserService iUserService;
    @Resource
    private TopicMapper topicMapper;

    @Override
    public List<NotifyVO> getNotifyListByType(String receiver, int type) {
        User user = iUserService.getById(receiver);

        if(user == null){
            try {
                ErrorResponse.sendJsonMessage(USER_NOT_EXISTS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        List<Notify> notifies = this.getBaseMapper().selectList(new LambdaQueryWrapper<Notify>()
                .eq(Notify::getReceiver,receiver)
                .eq(Notify::getType, type)
                .orderByDesc(Notify::getCreateTime)
        );
        System.out.println(notifies);
        List<NotifyVO> notifyVOS = new ArrayList<>();
        for (Notify notify : notifies) {
            notifyVOS.add(this.changeNotifyToNotifyVO(notify));
        }
        return notifyVOS;
    }

    @Override
    public Notify createNotify(String notifier, String receiver, String targetId, int type) {
        Notify notify = Notify.builder()
                .notifier(notifier)
                .receiver(receiver)
                .targetId(targetId)
                .type(type)
                .createTime(new Date())
                .status(NotifyStatusEnum.UNREAD.getStatus())
                .build();
        this.baseMapper.insert(notify);
        return notify;

    }

    @Override
    public NotifyVO changeNotifyToNotifyVO(Notify notify) {
        NotifyVO notifyVO = new NotifyVO();
        notifyVO.setId(notify.getId());
        notifyVO.setNotifier(notify.getNotifier());
        notifyVO.setStatus(notify.getStatus());
        notifyVO.setCreateTime(notify.getCreateTime());
        NotifyTypeEnum notifyTypeEnum = NotifyTypeEnum.getEnumByType(notify.getType());
        notifyVO.setTitle(notifyTypeEnum.getName());
        notifyVO.setTargetId(notify.getTargetId());
        return notifyVO;
    }

    @Override
    public NotifyCountVO countNotify(String receiver) {
        NotifyCountVO notifyCountVO = new NotifyCountVO();
        notifyCountVO.setAllNotify(this.baseMapper.selectCount(new QueryWrapper<Notify>()
                .eq("receiver", receiver)
                .eq("status", NotifyStatusEnum.UNREAD.getStatus())));

        notifyCountVO.setPostPraise(this.baseMapper.selectCount(new QueryWrapper<Notify>()
                .eq("receiver", receiver)
                .eq("status", NotifyStatusEnum.UNREAD.getStatus())
                .eq("type", NotifyTypeEnum.POST_PRAISE.getTargetType())));

        notifyCountVO.setPostCollection(this.baseMapper.selectCount(new QueryWrapper<Notify>()
                .eq("receiver", receiver)
                .eq("status", NotifyStatusEnum.UNREAD.getStatus())
                .eq("type", NotifyTypeEnum.POST_COLLECT.getTargetType())));

        notifyCountVO.setPostComment(this.baseMapper.selectCount(new QueryWrapper<Notify>()
                .eq("receiver", receiver)
                .eq("status", NotifyStatusEnum.UNREAD.getStatus())
                .eq("type", NotifyTypeEnum.POST_COLLECT.getTargetType())));

        notifyCountVO.setCommentComment(this.baseMapper.selectCount(new QueryWrapper<Notify>()
                .eq("receiver", receiver)
                .eq("status", NotifyStatusEnum.UNREAD.getStatus())
                .eq("type", NotifyTypeEnum.COMMENT_COMMENT.getTargetType())));

        notifyCountVO.setUserSubscribe(this.baseMapper.selectCount(new QueryWrapper<Notify>()
                .eq("receiver", receiver)
                .eq("status", NotifyStatusEnum.UNREAD.getStatus())
                .eq("type", NotifyTypeEnum.USER_SUBSCRIBE.getTargetType())));
        return notifyCountVO;
    }
}
