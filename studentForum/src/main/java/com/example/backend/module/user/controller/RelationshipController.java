package com.example.backend.module.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.exception.ApiAsserts;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.user.entity.Follow;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IFollowService;
import com.example.backend.module.user.service.IUserService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    @Resource
    private IFollowService bmsFollowService;

    @Resource
    private IUserService umsUserService;

    @Resource
    private INotifyService iNotifyService;


    // 关注
    // userId 为被关注着id，从Json中获取，USER_NAME 为关注者id，从token中获取
    /**
     * 关注
     * @param parentId
     * @return
     */
    @LoginRequired(allowAll = true)
    @PostMapping("/subscribe")
    public ApiResult<Object> handleFollow(@RequestBody() String parentId) {
        User user = AuthInterceptor.getCurrentUser();
        if (parentId.equals(user.getId())) {
            ApiAsserts.fail("不允许关注自己");
        }
        // 查询二者在follow表中是否有关联记录
        Follow one = bmsFollowService.getOne(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getParentId, parentId)
                        .eq(Follow::getFollowerId, user.getId()));
        if (!ObjectUtils.isEmpty(one)) {
            // 有记录，说明已关注
            return ApiResult.failed("已关注");
        }
        // 没记录，则增添一条关联记录即关注成功
        Follow follow = new Follow();
        follow.setParentId(parentId);
        follow.setFollowerId(user.getId());
        bmsFollowService.save(follow);

        //新增关注通知
        iNotifyService.createNotify(parentId,user.getId(),"5", null);

        return ApiResult.success(null, "关注成功");
    }

    /**
     * 取关
     * @param parentId
     * @return
     */
    @LoginRequired(allowAll = true)
    @PostMapping("/unsubscribe")
    public ApiResult<Object> handleUnFollow(@RequestBody() String parentId) {
        User user = AuthInterceptor.getCurrentUser();
        // 查询二者在follow表中是否有关联记录
        Follow one = bmsFollowService.getOne(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getParentId, parentId)
                        .eq(Follow::getFollowerId, user.getId()));
        if (ObjectUtils.isEmpty(one)) {
            // 没有记录，说明还未关注
            return ApiResult.failed( "未关注");
        }
        // 有记录，说明关注了，删除该关联记录即可
        bmsFollowService.remove(new LambdaQueryWrapper<Follow>().eq(Follow::getParentId, parentId)
                .eq(Follow::getFollowerId, user.getId()));
        // 取消关注
        iNotifyService.remove(new LambdaQueryWrapper<Notify>().eq(Notify::getUserId, parentId)
                .eq(Notify::getFromId, user.getId())
                .eq(Notify::getAction, "5")
        );

        return ApiResult.success(null, "取关成功");
    }


    /**
     * 校验，判断指定用户和当前用户是否有关注关系
     * @param topicUserId
     * @return
     */
    @LoginRequired(allowAll = true)
    @GetMapping("/validate/{topicUserId}")
    public ApiResult<Map<String, Object>> isFollow(@PathVariable("topicUserId") String topicUserId) {
        User user = AuthInterceptor.getCurrentUser();
        Map<String, Object> map = new HashMap<>(16);
        map.put("hasFollow", false);
        if (!ObjectUtils.isEmpty(user)) {
            Follow one = bmsFollowService.getOne(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getParentId, topicUserId)
                    .eq(Follow::getFollowerId, user.getId()));
            if (!ObjectUtils.isEmpty(one)) {
                map.put("hasFollow", true);
            }
        }
        return ApiResult.success(map);
    }

    /**
     * 获取关注列表
     * @return
     */
    @LoginRequired(allowAll = true)
    @RequestMapping("/subscription")
    public ApiResult<List<Map<String,String>>> getAllSubscriptions(){
        User user = AuthInterceptor.getCurrentUser();
        List<Follow> list = bmsFollowService.list(new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, user.getId()));
        List<Map<String,String>> subList = new ArrayList<>();
        for (Follow follow : list) {
            User subUser =  umsUserService.getById(follow.getParentId());
            Map<String,String> map = new HashMap<>();
            map.put("id",follow.getParentId());
            map.put("name",subUser.getUsername());
            subList.add(map);
        }
        return ApiResult.success(subList);
    }
}