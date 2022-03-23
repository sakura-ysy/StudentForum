package com.example.backend.module.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.annotation.OperLog;
import com.example.backend.common.api.ApiResult;
import com.example.backend.common.enums.NotifyTypeEnum;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.module.notify.entity.Notify;
import com.example.backend.module.notify.service.INotifyService;
import com.example.backend.module.post.dto.CommentDTO;
import com.example.backend.module.post.entity.*;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.post.service.ICommentService;
import com.example.backend.module.post.service.IPostCollectService;
import com.example.backend.module.post.service.IPostPraiseService;
import com.example.backend.module.post.vo.CommentVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "用户-帖子交互相关接口")
@RestController
@RequestMapping("/api/react")
public class UserPostReactController {

    @Resource
    private IUserService iUserService;
    @Resource
    private ICommentService bmsCommentService;
    @Resource
    private IUserService umsUserService;
    @Resource
    private IPostPraiseService iPostPraiseService;
    @Resource
    private IPostCollectService iPostCollectService;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private INotifyService iNotifyService;

    @ApiOperation("获取帖子全部评论(树)")
    @GetMapping("/comment/{postId}")
    public ApiResult<List<CommentVO>> getCommentsTreeByPostID(
            @ApiParam("帖子Id") @PathVariable("postId") String postId) throws IOException {
        List<CommentVO> list = bmsCommentService.getCommentTreeByPostId(postId);
        return ApiResult.success(list);
    }

    /**
     * 获取一级评论
     * @param postId
     * @return
     */
    @ApiOperation("获取帖子的一级评论")
    @GetMapping("/comment/getI")
    public ApiResult<List<CommentVO>> getCommentsByPostID(
            @ApiParam("帖子id") @RequestParam(value = "postId") String postId) throws IOException {
        List<CommentVO> lstBmsComment = bmsCommentService.getFirstLevelCommentsByTopicID(postId);
        return ApiResult.success(lstBmsComment);
    }


    /**
     * 发起一级评论
     * @param dto
     * @return
     */
    @ApiOperation("发布一级评论")
    @OperLog(operModul = "123", operType = "get", operDesc = "123")
    @LoginRequired(allowAll = true)
    @PostMapping("/comment/addI")
    public ApiResult<Comment> addFirstComment(@RequestBody CommentDTO dto) {
        User user = AuthInterceptor.getCurrentUser();
        Comment comment = bmsCommentService.createFirstLevelComment(dto, user);

        // 新增一级评论通知
        Post post = topicMapper.selectById(dto.getPostId());
        iNotifyService.createNotify(user.getId(), post.getUserId(), post.getId(), NotifyTypeEnum.POST_COMMENT.getTargetType());
        return ApiResult.success(comment);
    }

    /**
     * 获取二级评论
     * @param postId
     * @param parentId
     * @return
     */
    @ApiOperation("获取二级评论")
    @GetMapping("/comment/getII")
    public ApiResult<List<CommentVO>> getCommentsByParentID(
            @ApiParam("帖子id") @RequestParam(value = "postId") String postId,
            @ApiParam("父评论id") @RequestParam(value = "parentId") String parentId) {
        System.out.println(parentId);
        List<CommentVO> lstBmsComment = bmsCommentService.getSecondLevelCommentsByParentID(postId, parentId);
        return ApiResult.success(lstBmsComment);
    }

    /**
     * 发起二级评论
     * @param dto
     * @return
     */
    @ApiOperation("发布二级评论")
    @LoginRequired(allowAll = true)
    @PostMapping("/comment/addII")
    public ApiResult<Comment> addSecondComment(@RequestBody CommentDTO dto) {
        User user = AuthInterceptor.getCurrentUser();
        Comment comment = bmsCommentService.createSecondLevelComment(dto, user);

        // 新增二级评论通知
        Post post = topicMapper.selectById(dto.getPostId());
        Comment parentComment = bmsCommentService.getBaseMapper().selectById(dto.getParentId());

        if (comment.getReplyToId() != null) {
            iNotifyService.createNotify(user.getId(), dto.getReplyToId(), comment.getId(), NotifyTypeEnum.COMMENT_COMMENT.getTargetType());
        }
        iNotifyService.createNotify(user.getId(), comment.getUserId(), comment.getId(), NotifyTypeEnum.COMMENT_COMMENT.getTargetType());

        return ApiResult.success(comment);
    }

    /**
     * 点赞
     * @param id
     * @return
     */
    @ApiOperation("点赞")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/praise/{postId}",method = RequestMethod.POST)
    public ApiResult<String> praise(
            @ApiParam("帖子id")@PathVariable("postId") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Integer statusCode = iPostPraiseService.executePraise(id,user.getUsername());
        switch (statusCode){
            case -1:
                return ApiResult.failed("帖子不存在");
            case 0:
                return ApiResult.failed("已点赞！");
        }

        // 新增点赞通知
        Post post = topicMapper.selectById(id);
        iNotifyService.createNotify(user.getId(), post.getUserId(), post.getId(), NotifyTypeEnum.POST_PRAISE.getTargetType());

        return ApiResult.success(null,"点赞成功");
    }

    /**
     * 取消点赞
     * @param id
     * @return
     */
    @ApiOperation("取消点赞")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/unpraise/{postId}",method = RequestMethod.POST)
    public ApiResult<String> unPraise(
            @ApiParam("帖子id")@PathVariable("postId") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Integer statusCode = iPostPraiseService.executeUnPraise(id,user.getUsername());
        switch (statusCode){
            case -1:
                return ApiResult.failed("帖子不存在");
            case 0:
                return ApiResult.failed("还未点赞");
        }
        // 取消点赞通知
        Post post = topicMapper.selectById(id);
        iNotifyService.remove(new LambdaQueryWrapper<Notify>().eq(Notify::getNotifier, user.getId())
                .eq(Notify::getTargetId, post.getId())
                .eq(Notify::getType, NotifyTypeEnum.POST_PRAISE)
        );

        return ApiResult.success(null,"取赞成功");
    }

    /**
     * 收藏
     * @param id
     * @return
     */
    @ApiOperation("收藏")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/collect/{postId}",method = RequestMethod.POST)
    public ApiResult<String> collect(
            @ApiParam("帖子id")@PathVariable("postId") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Integer statusCode = iPostCollectService.executeCollect(id,user.getUsername());
        switch (statusCode){
            case -1:
                return ApiResult.failed("帖子不存在");
            case 0:
                return ApiResult.failed("已收藏！");
        }
        // 新增收藏通知
        Post post = topicMapper.selectById(id);
        iNotifyService.createNotify(user.getId(), post.getUserId(), post.getId(), NotifyTypeEnum.POST_COLLECT.getTargetType());

        return ApiResult.success(null,"收藏成功");
    }

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @ApiOperation("取消收藏")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/uncollect/{postId}",method = RequestMethod.POST)
    public ApiResult<String> unCollect(
            @ApiParam("帖子id")@PathVariable("postId") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Integer statusCode = iPostCollectService.executeUnCollect(id,user.getUsername());
        switch (statusCode){
            case -1:
                return ApiResult.failed("帖子不存在");
            case 0:
                return ApiResult.failed("还未收藏");
        }
        // 取消收藏通知
        Post post = topicMapper.selectById(id);
        iNotifyService.remove(new LambdaQueryWrapper<Notify>().eq(Notify::getNotifier, user.getId())
                .eq(Notify::getTargetId, post.getId())
                .eq(Notify::getType, NotifyTypeEnum.POST_COMMENT.getTargetType())
        );
        return ApiResult.success(null,"取消收藏成功");
    }

    /**
     * 用户是否点赞
     * @param id
     * @return
     */
    @ApiOperation("判断用户是否点赞")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/ispraise/{postId}",method = RequestMethod.GET)
    public ApiResult<Boolean> isPraise(
            @ApiParam("帖子id")@PathVariable("postId") String id){
        User user = AuthInterceptor.getCurrentUser();
        PostPraise postPraise = iPostPraiseService.getBaseMapper().selectOne(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getUserId, user.getId()).eq(PostPraise::getPostId, id));
        if (ObjectUtils.isEmpty(postPraise)){
            return ApiResult.success(false);
        }
        return ApiResult.success(true);
    }

    /**
     * 用户是否收藏
     * @param id
     * @return
     */
    @ApiOperation("判断用户是否收藏")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/iscollect/{postId}",method = RequestMethod.GET)
    public ApiResult<Boolean> isCollect(
            @ApiParam("帖子id")@PathVariable("postId") String id){
        User user = AuthInterceptor.getCurrentUser();
        PostCollect postCollect = iPostCollectService.getBaseMapper().selectOne(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getUserId, user.getId()).eq(PostCollect::getPostId, id));
        if (ObjectUtils.isEmpty(postCollect)){
            return ApiResult.success(false);
        }
        return ApiResult.success(true);
    }
}
