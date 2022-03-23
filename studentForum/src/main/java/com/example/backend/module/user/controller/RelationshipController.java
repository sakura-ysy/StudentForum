package com.example.backend.module.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.enums.NotifyTypeEnum;
import com.example.backend.common.exception.ApiAsserts;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.user.entity.Follow;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IFollowService;
import com.example.backend.module.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.scene.Parent;
import org.junit.internal.runners.statements.Fail;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.backend.common.api.ApiErrorCode.FAILED;

@Api(tags = "用户关系相关接口")
@RestController
@RequestMapping("/api/relationship")
public class RelationshipController {

    @Resource
    private IFollowService bmsFollowService;

    @Resource
    private IUserService umsUserService;

    @Resource
    private INotifyService iNotifyService;


    // 关注
    // userId 为被关注者id，从Json中获取，USER_NAME 为关注者id，从token中获取
    /**
     * 关注
     * @param parentId
     * @return
     */
    @ApiOperation("关注")
    @LoginRequired(allowAll = true)
    @GetMapping("/subscribe/{userId}")
    public ApiResult<Object> handleFollow(
            @ApiParam("被关注者userId") @PathVariable("userId") String parentId) {
        User user = AuthInterceptor.getCurrentUser();
        if (parentId.equals(user.getId())) {
            ApiAsserts.fail(FAILED,"不允许关注自己");
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
        iNotifyService.createNotify(user.getId(), parentId, parentId, NotifyTypeEnum.USER_SUBSCRIBE.getTargetType());

        return ApiResult.success(null, "关注成功");
    }

    /**
     * 取关, userId为被取关者id
     * @param map
     * @return
     */
    @ApiOperation("取关")
    @LoginRequired(allowAll = true)
    @PostMapping("/unsubscribe")
    public ApiResult<Object> handleUnFollow(
            @ApiParam("被取关者ID，仅需传入userId字段")@RequestBody() Map<String,String> map) {
        User user = AuthInterceptor.getCurrentUser();
        // 查询二者在follow表中是否有关联记录
        String parentId = map.get("userId");
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
        iNotifyService.remove(new LambdaQueryWrapper<Notify>().eq(Notify::getNotifier, user.getId())
                .eq(Notify::getReceiver, parentId)
                .eq(Notify::getType, NotifyTypeEnum.USER_SUBSCRIBE.getTargetType())
        );

        return ApiResult.success(null, "取关成功");
    }


    /**
     * 校验，判断指定用户和当前用户是否有关注关系
     * @param parentId
     * @return
     */
    @ApiOperation("判断是有关注关系")
    @LoginRequired(allowAll = true)
    @GetMapping("/validate/{userId}")
    public ApiResult<Map<String, Object>> isFollow(
            @ApiParam("目标用户id")@PathVariable("userId") String parentId) {
        User user = AuthInterceptor.getCurrentUser();
        Map<String, Object> map1 = new HashMap<>(16);
        map1.put("hasFollow", false);
        if (!ObjectUtils.isEmpty(user)) {
            Follow one = bmsFollowService.getOne(new LambdaQueryWrapper<Follow>()
                    .eq(Follow::getParentId, parentId)
                    .eq(Follow::getFollowerId, user.getId()));
            if (!ObjectUtils.isEmpty(one)) {
                map1.put("hasFollow", true);
            }
        }
        return ApiResult.success(map1);
    }

    /**
     * 获取关注列表
     * @return
     */
    @ApiOperation("获取当前用户关注列表")
    @LoginRequired(allowAll = true)
    @GetMapping(value = "/subscription")
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