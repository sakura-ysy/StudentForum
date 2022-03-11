package com.example.backend.module.notify.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notify")
public class NotifyController {
    @Resource
    private INotifyService iNotifyService;

    @Resource
    private IUserService iUserService;

    // 所有通知
    @LoginRequired(allowAll = true)
    @GetMapping("/all")
    public ApiResult<List<Notify>> getNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    // 通知类型
    // 1.点赞 2.评论 3.收藏 4，回复 5.关注
    @LoginRequired(allowAll = true)
    @GetMapping("/praise")
    public ApiResult<List<Notify>> getPraiseNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getPraiseNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @LoginRequired(allowAll = true)
    @GetMapping("/comment")
    public ApiResult<List<Notify>> getCommentNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getCommentNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @LoginRequired(allowAll = true)
    @GetMapping("/collect")
    public ApiResult<List<Notify>> getCollectNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getCollectNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @LoginRequired(allowAll = true)
    @GetMapping("/reply")
    public ApiResult<List<Notify>> getReplyNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getReplyNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @LoginRequired(allowAll = true)
    @GetMapping("/follow")
    public ApiResult<List<Notify>> getFollowNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getFollowNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @LoginRequired(allowAll = true)
    @GetMapping("/read")
    public ApiResult<Notify> readNotifyById(@RequestParam("notifyId") String notifyId) {
        User user = AuthInterceptor.getCurrentUser();
        Notify notify = iNotifyService.getBaseMapper().selectById(notifyId);
        if (!notify.getUserId().equals(user.getId())) {
            return ApiResult.failed("产生错误，该用户不可能读取到该消息");
        }
        iNotifyService.getBaseMapper().update(notify, new UpdateWrapper<Notify>().eq("id", notifyId)
                .set("is_read", 1));
        return ApiResult.success(notify);
    }

    @LoginRequired(allowAll = true)
    @GetMapping("/count")
    public ApiResult<Integer> countNotify(@RequestParam("actionId") String actionId) {
        User user = AuthInterceptor.getCurrentUser();
        Integer countNotify = iNotifyService.countNotify(user.getId(), actionId);
        return ApiResult.success(countNotify);
    }
}
