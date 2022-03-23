package com.example.backend.module.notify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.vo.NotifyCountVO;
import com.example.backend.module.notify.vo.NotifyVO;

import java.util.List;


public interface INotifyService extends IService<Notify> {
    /**
     * 创建通知
     * @param notifier 通知者
     * @param receiver 被通知者
     * @param targetId 通知来源id
     * @param type 通知类型
     */
    Notify createNotify(String notifier, String receiver, String targetId , int type);

    List<NotifyVO> getNotifyListByType(String receiver, int type);

    NotifyCountVO countNotify(String receiver);

    NotifyVO changeNotifyToNotifyVO(Notify notify);
}
