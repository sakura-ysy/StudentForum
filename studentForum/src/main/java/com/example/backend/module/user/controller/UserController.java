package com.example.backend.module.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.annotation.OperLog;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.post.vo.PostVO;
import com.example.backend.module.user.entity.UserGitLab;
import com.example.backend.module.user.entity.UserRole;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.entity.PostCollect;
import com.example.backend.module.post.mapper.PostCollectMapper;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.post.service.IPostService;
import com.example.backend.module.user.dto.LoginDTO;
import com.example.backend.module.user.dto.RegisterDTO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.UserMapper;
import com.example.backend.module.user.service.IUserGitLabService;
import com.example.backend.module.user.service.IUserService;
import com.example.backend.module.user.vo.ProfileVO;
import com.example.backend.utils.MD5Utils;
import com.example.backend.utils.SendMessageUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.geometry.Pos;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @RestController 相当于 @Controller + @ResponseBody
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private IUserService iUserService;
    @Resource
    private IPostService iPostService;
    @Resource
    private IUserGitLabService iUserGitLabService;
    @Resource
    private UserController userController;

    @Resource
    private UserMapper userMapper;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private PostCollectMapper postCollectMapper;

    /**
     * 注册
     * @param dto
     * @return
     */
    @ApiOperation("用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        User user = iUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("账号或邮箱已存在！");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }


    /**
     * 发送短信验证码
     * @param map1
     * @return
     */
    @ApiOperation("发送短信验证码")
    @RequestMapping(value = "/valicode", method = RequestMethod.GET)
    public ApiResult<Map<String,String>> getValiCode(
            @ApiParam("手机号，仅需传入mobile字段")@RequestBody Map<String,String> map1) {
        String desMobile = map1.get("mobile");
        String random = SendMessageUtils.getRandomCode(6);
        //SendMessageUtils.send("SMS账户","接口秘钥","目标号码","发送内容");
        Integer resultCode = SendMessageUtils.send("yesiyuan","d41d8cd98f00b204e980",desMobile,"验证码:"+ random);
        String message = SendMessageUtils.getMessage(resultCode);
        Map<String,String> map = new HashMap<>(16) ;
        map.put("valicode", MD5Utils.getPwd(random)) ;
        if (resultCode > 0)
            return ApiResult.success(map,message);
        return ApiResult.failed(message);
    }

    /**
     * 登录
     * @param dto
     * @return
     */
    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        User user = iUserService.getUserByUsername(dto.getUsername());
       String token = iUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(token)) {
            return ApiResult.failed("账号密码错误");
        }

        return userController.loginSuccess(token);
    }

    /**
     * 登录成功的log日志
     */
    @OperLog(operModul = "User",operType = "post", operDesc = "用户登录")
    public ApiResult<Map<String,String>> loginSuccess(String token){
        Map<String, String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map, "登录成功");
    }

    /**
     * 获取当前用户信息
     * @return
     */
    @ApiOperation("获取当前用户信息")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<User> getUser() {
        User user = AuthInterceptor.getCurrentUser();
        return ApiResult.success(user);
    }

    /**
     * 退出
     * @return
     */
    @ApiOperation("用户登出")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/logout",method = RequestMethod.PUT)
    public ApiResult<Object> logOut() {
        userController.logOutSuccess();
        return ApiResult.success(null,"注销成功") ;
    }

    /**
     * 退出登录的log日志
     */
    @OperLog(operModul = "User",operType = "get", operDesc = "退出登录")
    public void logOutSuccess(){
    }

    /**
     * 查询用户
     * @param username
     * @return
     */
    @ApiOperation("根据用户名查用户")
    @GetMapping("/search/name/{username}")
    public ApiResult<Map<String, Object>> getUserByName(
            @ApiParam("用户名username")@PathVariable("username")String username) {
        Map<String, Object> map1 = new HashMap<>(16);
        User user = iUserService.getUserByUsername(username);
        if (user == null)
            return ApiResult.failed("用户不存在");
        List<Post> posts = iPostService.getBaseMapper().selectList(new LambdaQueryWrapper<Post>().eq(Post::getUserId, user.getId()));
        map1.put("user", user);
        map1.put("posts",posts) ;
        return ApiResult.success(map1);
    }


    /**
     * 通过id查用户
     * @param userid
     * @return
     */
    @ApiOperation("根据用户ID查用户")
    @GetMapping("/search/id/{id}")
    public ApiResult<User> getUserById(
            @ApiParam("用户id")@PathVariable("id") String userid) {
        User user = iUserService.getBaseMapper().selectById(userid);
        if (ObjectUtils.isEmpty(user)){
            return ApiResult.failed("用户不存在");
        }
        return ApiResult.success(user);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @ApiOperation("编辑当前用户信息")
    @PostMapping("/update")
    public ApiResult<User> updateUser(@RequestBody User user) {
        iUserService.updateById(user);
        return ApiResult.success(user);
    }

    /**
     * 获取用户的全部收藏帖子
     * @return
     */
    @ApiOperation("获取当前用户全部收藏帖子")
    @LoginRequired(allowAll = true)
    @GetMapping(value = "/collection")
    public ApiResult<List<PostVO>> getAllCollections(){
        User user = AuthInterceptor.getCurrentUser();
        List<PostCollect> list = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getUserId, user.getId()));
        if (list==null)
            return ApiResult.success(null);
        List<PostVO> postList = new ArrayList<>();
        for (PostCollect postCollect : list) {
            String postId = postCollect.getPostId();
            Post post = topicMapper.selectById(postId);
            PostVO postVO = iPostService.changePostToPostVO(post);
            postList.add(postVO);
        }
        return ApiResult.success(postList);
    }

    /**
     * 获取当前用户全部帖子
     * @return
     */
    @ApiOperation("获取当前用户全部帖子")
    @LoginRequired(allowAll = true)
    @GetMapping("/post/list")
    public ApiResult<List<PostVO>> getAllPosts() throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        List<PostVO> postVOs = iPostService.getAllPostForUser(user.getId());
        return ApiResult.success(postVOs);
    }

    /**
     * 更新用户别名
     * @param newAlias 用户新别名
     * @return
     */
    @ApiOperation("跟新用户昵称")
    @LoginRequired(allowAll = true)
    @PostMapping("/update/alias")
    public ApiResult<ProfileVO> updateUser(
            @ApiParam("昵称，仅需传入alias字段")@RequestBody Map<String, String> newAlias) {
        System.out.println(newAlias);
        User user = AuthInterceptor.getCurrentUser();
        user.setAlias(newAlias.get("alias"));
        iUserService.getBaseMapper().updateById(user);
        return ApiResult.success(iUserService.getUserProfile(user.getId()));
    }

    /**
     * 更新用户头像
     * @param file 上传的头像文件
     * @return
     * @throws IOException
     */
    @ApiOperation("用户上传头像")
    @LoginRequired(allowAll = true)
    @PostMapping("/update/avatar")
    public ApiResult<ProfileVO> updateUserAvatar(
            @ApiParam("本地头像文件") MultipartFile file) throws IOException {
        /*七牛云配置信息
        accessKey: asn5hMMKnHCgOOnJw-f_0oh1ATDSmm1N7kTHS5jn
        secretKey: A1eMsw3pFjiLLO_cdm7sXF28xydYUp7ExI5PKOLb
        空间名
        bucket: huangdou
        区域名
        zone: huanan
        域名
        domain: qxksa1qbs.hn-bkt.clouddn.com
        */
        // 改头像用户
        User user = AuthInterceptor.getCurrentUser();

        String accessKey = "asn5hMMKnHCgOOnJw-f_0oh1ATDSmm1N7kTHS5jn";
        String secretKey = "A1eMsw3pFjiLLO_cdm7sXF28xydYUp7ExI5PKOLb";
        String bucket = "huangdou";
        String domain = "qxksa1qbs.hn-bkt.clouddn.com";
        Configuration configuration = new Configuration(Region.huanan());
        // 文件上传器
        UploadManager manager = new UploadManager(configuration);

        // 不指定密钥，默认使用文件hash
        String key = null;
        try {
            // 文件转化为字节流
            byte[] uploadBytes = file.getBytes();
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            // 上传凭证
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = manager.put(byteInputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                /*System.out.println(putRet.key);
                System.out.println(putRet.hash);*/
                user.setAvatar(domain + "/" + putRet.key);
                iUserService.getBaseMapper().updateById(user);
                return ApiResult.success(iUserService.getUserProfile(user.getId()));
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    ex2.printStackTrace();
                    return ApiResult.failed("错误原因异常");
                }
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return ApiResult.failed("上传头像失败");
        }
        return ApiResult.success(iUserService.getUserProfile(user.getId()));
    }

    /**
     * 绑定GitLab用户名
     * @return
     */
    @ApiOperation("绑定GitLab用户名")
    @LoginRequired(allowAll = true)
    @PostMapping(value = "/bindGitLab")
    public ApiResult<UserGitLab> bindGitLabUsername(
            @ApiParam("传入GitLab用户名")@RequestBody String gitLabUsername
    ) {
        User user = AuthInterceptor.getCurrentUser();
        return ApiResult.success(iUserGitLabService.bindGitLabUsername(user, gitLabUsername));
    }

    @ApiOperation("解绑GitLab用户名")
    @LoginRequired(allowAll = true)
    @PostMapping(value = "/unbbindGitLab")
    public ApiResult<Object> unbindGitLabUsername() {
        User user = AuthInterceptor.getCurrentUser();
        String gitLabUsername = iUserGitLabService.getGitLabUsernameByUser(user);
        if (gitLabUsername == null) {
            return ApiResult.failed("未绑定");
        }
        if (iUserGitLabService.unbindGitLabUsername(user)) {
            return ApiResult.success("解绑成功");
        }
        else {
            return ApiResult.failed("解绑失败");
        }
    }



}