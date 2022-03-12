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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "消息通知相关接口")
@RestController
@RequestMapping("/api/notify")
public class NotifyController {
    @Resource
    private INotifyService iNotifyService;

    @Resource
    private IUserService iUserService;

    // 所有通知
    @ApiOperation("获取用户所有消息通知")
    @LoginRequired(allowAll = true)
    @GetMapping("/all")
    public ApiResult<List<Notify>> getNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    // 通知类型
    // 1.点赞 2.评论 3.收藏 4，回复 5.关注
    @ApiOperation("获取点赞通知")
    @LoginRequired(allowAll = true)
    @GetMapping("/praise")
    public ApiResult<List<Notify>> getPraiseNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getPraiseNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @ApiOperation("获取评论通知")
    @LoginRequired(allowAll = true)
    @GetMapping("/comment")
    public ApiResult<List<Notify>> getCommentNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getCommentNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @ApiOperation("获取收藏通知")
    @LoginRequired(allowAll = true)
    @GetMapping("/collect")
    public ApiResult<List<Notify>> getCollectNotifyByUserId() {
        User user = AuthInterceptor.getCurrentUser();
        List<Notify> notifies = iNotifyService.getCollectNotifyByUserId(user.getId());
        return ApiResult.success(notifies);
    }

    @ApiOperation("获取回复通知")
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

    @ApiOperation("读通知")
    @LoginRequired(allowAll = true)
    @GetMapping("/read/{id}")
    public ApiResult<Notify> readNotifyById(
             @ApiParam("通知id") @PathVariable("id") String notifyId) {
        User user = AuthInterceptor.getCurrentUser();
        Notify notify = iNotifyService.getBaseMapper().selectById(notifyId);
        if (notify==null)
                return ApiResult.failed("消息不存在");
        if (!notify.getUserId().equals(user.getId())) {
            return ApiResult.failed("产生错误，该用户不可能读取到该消息");
        }
        iNotifyService.getBaseMapper().update(notify, new UpdateWrapper<Notify>().eq("id", notifyId)
                .set("is_read", 1));
        return ApiResult.success(notify);
    }

    @ApiOperation("获取未读通知")
    @LoginRequired(allowAll = true)
    @GetMapping("/count/noread/{actionId}")
    public ApiResult<Integer> countNotify(
            @ApiParam("传入actionId, 代表通知的类型id, 1.点赞 2.评论 3.收藏 4，回复 5.关注, String型")@PathVariable("actionId") String id) {
        User user = AuthInterceptor.getCurrentUser();
        Integer countNotify = iNotifyService.countNotify(user.getId(), id);
        return ApiResult.success(countNotify);
    }
}
