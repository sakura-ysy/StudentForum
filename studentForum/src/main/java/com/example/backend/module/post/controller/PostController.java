package com.example.backend.module.post.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.post.dto.CreateTopicDTO;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.entity.PostCollect;
import com.example.backend.module.post.entity.PostPraise;
import com.example.backend.module.post.entity.Tag;
import com.example.backend.module.post.mapper.PostCollectMapper;
import com.example.backend.module.post.mapper.PostPraiseMapper;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "帖子相关接口")
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Resource
    private IPostService iPostService;
    @Resource
    private IUserService umsUserService;

    @Resource
    private PostPraiseMapper postPraiseMapper;
    @Resource
    private PostCollectMapper postCollectMapper;
    @Resource
    private TopicMapper topicMapper;

    // form-data 传参

    /**
     * 返回所有帖子
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation("分页获取帖子列表")
    @GetMapping("/list")
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = iPostService.getList(new Page<>(pageNo, pageSize), "latest");
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }


    /**
     * 发布帖子
     * @param dto
     * @return
     */
    @ApiOperation("发布帖子")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<PostVO> create(@RequestBody CreateTopicDTO dto) {
        User user = AuthInterceptor.getCurrentUser();
        PostVO postVO = iPostService.create(dto, user);
        return ApiResult.success(postVO);
    }


    /**
     * 获取文章详情，以id为检索词
     * @param id
     * @return
     */
    @ApiOperation("获取帖子详情，id检索")
    @GetMapping("/{id}")
    public ApiResult<Map<String, Object>> view(
            @ApiParam("帖子id") @PathVariable("id") String id) throws IOException {
        Map<String, Object> map = iPostService.viewTopic(id);
        return ApiResult.success(map);
    }


    /**
     * 随机返回10篇帖子(排除当前帖子)，用于推荐
     * @param postId
     * @return
     */
    @ApiOperation("随机返回10篇帖子用于推荐")
    @GetMapping("/recommend/{id}")
    public ApiResult<List<Post>> getRecommend(
            @ApiParam("当前帖子id")@PathVariable("id") String postId){
        List<Post> topics = iPostService.getRecommend(postId);
        return ApiResult.success(topics) ;
    }

    /**
     * 更新帖子
     * @param post
     * @return
     */
    @ApiOperation("编辑帖子")
    @LoginRequired(allowAll = true)
    @PostMapping("/update")
    public ApiResult<PostVO> update(@Valid @RequestBody Post post) {
        User user = AuthInterceptor.getCurrentUser();
        // 判断待编辑帖子的作者id是否等于当前用户id，如果不等于则抛出message，结束
        Assert.isTrue(user.getId().equals(post.getUserId()), "非本人无权修改");
        post.setModifyTime(new Date());  // 重置改贴时间
        post.setContent(EmojiParser.parseToAliases(post.getContent()));  // 对emoji编码
        iPostService.updateById(post); // 更新表中对象
        return ApiResult.success(iPostService.changePostToPostVO(post));
    }


    /**
     * 删除帖子
     * @param id
     * @return
     */
    @ApiOperation("删除帖子")
    @LoginRequired(allowAll = true)
    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(
            @ApiParam("帖子id") @PathVariable("id") String id) {
        User user = AuthInterceptor.getCurrentUser();
        Post byId = iPostService.getById(id);
        Assert.notNull(byId, "来晚一步，话题已不存在");
        // 判断待删除帖子的作者id是否等于当前用户id，如果不等于则抛出message，结束
        Assert.isTrue(byId.getUserId().equals(user.getId()), "无法删除别人的帖子");
        // 删除
        iPostService.removeById(id);
        return ApiResult.success(null,"删除成功");
    }

    /**
     * 获取点赞数
     * @param postId
     * @return
     */
    @ApiOperation("获取点赞数")
    @GetMapping(value = "/praise/num/{id}")
    public ApiResult<Integer> praiseNumber(
            @ApiParam("帖子id")@PathVariable("id") String postId){
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post))
            return ApiResult.failed("帖子不存在");
        List<PostPraise> list = postPraiseMapper.selectList(new LambdaQueryWrapper<PostPraise>().eq(PostPraise::getPostId, postId));
        return ApiResult.success(list.size());
    }

    /**
     * 获取收藏数
     * @param postId
     * @return
     */
    @ApiOperation("获取收藏数")
    @GetMapping(value = "/collect/num/{id}")
    public ApiResult<Integer> collectNumber(
            @ApiParam("帖子id")@PathVariable("id") String postId){
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post))
            return ApiResult.failed("帖子不存在");
        List<PostCollect> list = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId, postId));
        return ApiResult.success(list.size());
    }

    @ApiOperation("关键词搜索, 分页返回帖子列表")
    @GetMapping("/search/list")
    public ApiResult<Page<PostVO>> searchList(@RequestParam(value = "keyword") String keyword,
                                              @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> results = iPostService.searchByKey(keyword, new Page<>(pageNum, pageSize));
        return ApiResult.success(results);
    }

    /**
     * 返回指定标签下的帖子
     * @param tagName
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("返回指定标签下的帖子列表")
    @GetMapping("/tag/list")
    public ApiResult<Map<String, Object>> getTopicsByTag(
            @ApiParam("tag名") @RequestParam(value = "name") String tagName,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        // TODO
        return null;
        // Map<String, Object> map = new HashMap<>(16);
        //
        // LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(Tag::getName, tagName);
        // Tag one = bmsTagService.getOne(wrapper);
        // Assert.notNull(one, "话题不存在，或已被管理员删除");
        // Page<Post> topics = bmsTagService.selectTopicsByTagId(new Page<>(page, size), one.getId());
        //
        // // 其他热门标签
        // Page<Tag> hotTags = bmsTagService.page(new Page<>(1, 10),
        //         new LambdaQueryWrapper<Tag>()
        //                 .notIn(Tag::getName, tagName)
        //                 .orderByDesc(Tag::getTopicCount));
        //
        // map.put("topics", topics);
        // map.put("hotTags", hotTags);
        //
        // return ApiResult.success(map);
    }

}
