package com.example.backend.module.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.api.ApiResult;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.entity.Tag;
import com.example.backend.module.post.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

// 获取指定标签下的帖子
@Api(tags = "标签相关接口")
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Resource
    private ITagService bmsTagService;

    /**
     * 返回指定标签下的帖子
     * @param tagName
     * @param page
     * @param size
     * @return
     */
    // @ApiOperation("返回指定标签下的帖子列表")
    // @GetMapping("/get/postlist")
    // public ApiResult<Map<String, Object>> getTopicsByTag(
    //         @ApiParam("tag名") @RequestParam(value = "name") String tagName,
    //         @RequestParam(value = "pageNo", defaultValue = "1") Integer page,
    //         @RequestParam(value = "size", defaultValue = "10") Integer size) {
    //
    //     Map<String, Object> map = new HashMap<>(16);
    //
    //     LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
    //     wrapper.eq(Tag::getName, tagName);
    //     Tag one = bmsTagService.getOne(wrapper);
    //     Assert.notNull(one, "话题不存在，或已被管理员删除");
    //     Page<Post> topics = bmsTagService.selectTopicsByTagId(new Page<>(page, size), one.getId());
    //
    //     // 其他热门标签
    //     Page<Tag> hotTags = bmsTagService.page(new Page<>(1, 10),
    //             new LambdaQueryWrapper<Tag>()
    //                     .notIn(Tag::getName, tagName)
    //                     .orderByDesc(Tag::getTopicCount));
    //
    //     map.put("topics", topics);
    //     map.put("hotTags", hotTags);
    //
    //     return ApiResult.success(map);
    // }

}
