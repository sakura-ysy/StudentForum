package com.example.backend.module.user.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.annotation.OperLog;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
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
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService iUserService;
    @Resource
    private IPostService iPostService;
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
     * @param desMobile
     * @return
     */
    @RequestMapping(value = "/valicode", method = RequestMethod.GET)
    public ApiResult<Map<String,String>> getValiCode(@RequestParam("mobile") String desMobile ) {
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
     * 依据token获取用户信息
     * @return
     */
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
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ApiResult<Object> logOut() {
        userController.logOutSuccess();
        StpUtil.logout();
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
     * @param pageNo
     * @param size
     * @return
     */
    @GetMapping("/{username}")
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> map = new HashMap<>(16);
        User user = iUserService.getUserByUsername(username);
        Assert.notNull(user, "用户不存在");
        Page<Post> page = iPostService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Post>().eq(Post::getUserId, user.getId()));
        map.put("user", user);
        map.put("topics", page);
        return ApiResult.success(map);
    }


    /**
     * 通过id查用户
     * @param userid
     * @return
     */
    @RequestMapping
    public ApiResult<User> getUserById(@RequestParam("userid") String userid) {
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
    @PostMapping("/update")
    public ApiResult<User> updateUser(@RequestBody User user) {
        iUserService.updateById(user);
        return ApiResult.success(user);
    }

    /**
     * 获取用户的全部收藏帖子
     * @return
     */
    @LoginRequired(allowAll = true)
    @RequestMapping("/collection")
    public ApiResult<List<Post>> getAllCollections(){
        User user = AuthInterceptor.getCurrentUser();
        List<PostCollect> list = postCollectMapper.selectList(new LambdaQueryWrapper<PostCollect>().eq(PostCollect::getUserId, user.getId()));
        List<Post> postList = new ArrayList<>();
        for (PostCollect postCollect : list) {
            String postId = postCollect.getPostId();
            Post post = topicMapper.selectById(postId);
            postList.add(post);
        }
        return ApiResult.success(postList);
    }

    /**
     * 更新用户别名
     * @param newAlias 用户新别名
     * @return
     */
    @LoginRequired(allowAll = true)
    @PostMapping("/update/alias")
    public ApiResult<ProfileVO> updateUser(@RequestBody Map<String, String> newAlias) {
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
    @LoginRequired(allowAll = true)
    @PostMapping("/update/avatar")
    public ApiResult<ProfileVO> updateUserAvatar(MultipartFile file) throws IOException {
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

}