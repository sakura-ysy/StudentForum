package com.example.backend.module.notify.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.enums.NotifyStatusEnum;
import com.example.backend.common.enums.NotifyTypeEnum;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.notify.vo.NotifyCountVO;
import com.example.backend.module.notify.vo.NotifyVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "消息通知相关接口")
@RestController
@RequestMapping("/api/notify")
public class NotifyController {
    @Resource
    private INotifyService iNotifyService;

    @Resource
    private IUserService iUserService;

    @ApiOperation("获取点赞通知列表")
    @LoginRequired(allowAll = true)
    @GetMapping("/praise")
    public ApiResult<List<NotifyVO>> getPraiseNotify() {
        User user = AuthInterceptor.getCurrentUser();
        List<NotifyVO> notifies = iNotifyService.getNotifyListByType(user.getId(), NotifyTypeEnum.POST_PRAISE.getTargetType());
        return ApiResult.success(notifies);
    }

    @ApiOperation("获取收藏通知列表")
    @LoginRequired(allowAll = true)
    @GetMapping("/collect")
    public ApiResult<List<NotifyVO>> getCollectNotify() {
        User user = AuthInterceptor.getCurrentUser();
        List<NotifyVO> notifies = iNotifyService.getNotifyListByType(user.getId(), NotifyTypeEnum.POST_COLLECT.getTargetType());
        return ApiResult.success(notifies);
    }
    @ApiOperation("获取评论通知列表")
    @LoginRequired(allowAll = true)
    @GetMapping("/comment")
    public ApiResult<List<NotifyVO>> getCommentNotify() {
        User user = AuthInterceptor.getCurrentUser();
        List<NotifyVO> notifies = iNotifyService.getNotifyListByType(user.getId(), NotifyTypeEnum.POST_COMMENT.getTargetType());
        return ApiResult.success(notifies);
    }
    @ApiOperation("获取回复通知列表")
    @LoginRequired(allowAll = true)
    @GetMapping("/reply")
    public ApiResult<List<NotifyVO>> getReplyNotify() {
        User user = AuthInterceptor.getCurrentUser();
        List<NotifyVO> notifies = iNotifyService.getNotifyListByType(user.getId(), NotifyTypeEnum.COMMENT_COMMENT.getTargetType());
        return ApiResult.success(notifies);
    }
    @ApiOperation("获取关注通知列表")
    @LoginRequired(allowAll = true)
    @GetMapping("/subscribe")
    public ApiResult<List<NotifyVO>> getFollowNotify() {
        User user = AuthInterceptor.getCurrentUser();
        List<NotifyVO> notifies = iNotifyService.getNotifyListByType(user.getId(), NotifyTypeEnum.USER_SUBSCRIBE.getTargetType());
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
        if (notify.getNotifier().equals(user.getId())) {
            return ApiResult.failed("产生错误，该用户不可能读取到该消息");
        }
        iNotifyService.getBaseMapper().update(notify, new UpdateWrapper<Notify>().eq("id", notifyId)
                .set("status", NotifyStatusEnum.READ.getStatus()));
        return ApiResult.success(notify);
    }

    @ApiOperation("获取所有种类未读通知数量")
    @LoginRequired(allowAll = true)
    @GetMapping("/count")
    public ApiResult<NotifyCountVO> countNotify() {
        User user = AuthInterceptor.getCurrentUser();
        NotifyCountVO notifyCountVO = iNotifyService.countNotify(user.getId());
        return ApiResult.success(notifyCountVO);
    }
}
