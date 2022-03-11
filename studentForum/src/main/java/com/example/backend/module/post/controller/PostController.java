package com.example.backend.module.post.controller;

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
import com.example.backend.module.post.mapper.PostCollectMapper;
import com.example.backend.module.post.mapper.PostPraiseMapper;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
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
     * @param tab
     * @param pageNo
     * @param pageSize
     * @return
     */
    @LoginRequired(allowCommon = true)
    @GetMapping("/list")
    public ApiResult<Page<PostVO>> list(@RequestParam(value = "tab", defaultValue = "latest") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<PostVO> list = iPostService.getList(new Page<>(pageNo, pageSize), tab);
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }


    /**
     * 发布帖子
     * @param dto
     * @return
     */
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<Post> create(@RequestBody CreateTopicDTO dto) {
        User user = AuthInterceptor.getCurrentUser();
        Post topic = iPostService.create(dto, user);
        return ApiResult.success(topic);
    }


    /**
     * 获取文章详情，以id为检索词
     * @param id
     * @return
     */
    @GetMapping()
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id) {
        Map<String, Object> map = iPostService.viewTopic(id);
        return ApiResult.success(map);
    }


    /**
     * 随机返回10篇帖子(排除当前帖子)，用于推荐
     * @param id
     * @return
     */
    @GetMapping("/recommend")
    public ApiResult<List<Post>> getRecommend(@RequestParam("topicId") String id){
        List<Post> topics = iPostService.getRecommend(id);
        return ApiResult.success(topics) ;
    }

    /**
     * 更新帖子
     * @param post
     * @return
     */
    @LoginRequired(allowAll = true)
    @PostMapping("/update")
    public ApiResult<Post> update(@Valid @RequestBody Post post) {
        User user = AuthInterceptor.getCurrentUser();
        // 判断待编辑帖子的作者id是否等于当前用户id，如果不等于则抛出message，结束
        Assert.isTrue(user.getId().equals(post.getUserId()), "非本人无权修改");
        post.setModifyTime(new Date());  // 重置改贴时间
        post.setContent(EmojiParser.parseToAliases(post.getContent()));  // 对emoji编码
        iPostService.updateById(post); // 更新表中对象
        return ApiResult.success(post);
    }


    /**
     * 删除帖子
     * @param id
     * @return
     */
    @LoginRequired(allowAll = true)
    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(@PathVariable("id") String id) {
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
    @RequestMapping(value = "/praise/num",method = RequestMethod.GET)
    public ApiResult<Integer> praiseNumber(@RequestParam("id") String postId){
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
    @RequestMapping(value = "/collect/num",method = RequestMethod.GET)
    public ApiResult<Integer> collectNumber(@RequestParam("id") String postId){
        Post post = topicMapper.selectById(postId);
        if (ObjectUtils.isEmpty(post))
            return ApiResult.failed("帖子不存在");
        List<PostCollect> list = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getPostId, postId));
        return ApiResult.success(list.size());
    }

}
