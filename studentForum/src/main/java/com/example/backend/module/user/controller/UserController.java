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

// @RestController ????????? @Controller + @ResponseBody
@Api(tags = "??????????????????")
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
     * ??????
     * @param dto
     * @return
     */
    @ApiOperation("????????????")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        User user = iUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("???????????????????????????");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }


    /**
     * ?????????????????????
     * @param map1
     * @return
     */
    @ApiOperation("?????????????????????")
    @RequestMapping(value = "/valicode", method = RequestMethod.GET)
    public ApiResult<Map<String,String>> getValiCode(
            @ApiParam("????????????????????????mobile??????")@RequestBody Map<String,String> map1) {
        String desMobile = map1.get("mobile");
        String random = SendMessageUtils.getRandomCode(6);
        //SendMessageUtils.send("SMS??????","????????????","????????????","????????????");
        Integer resultCode = SendMessageUtils.send("yesiyuan","d41d8cd98f00b204e980",desMobile,"?????????:"+ random);
        String message = SendMessageUtils.getMessage(resultCode);
        Map<String,String> map = new HashMap<>(16) ;
        map.put("valicode", MD5Utils.getPwd(random)) ;
        if (resultCode > 0)
            return ApiResult.success(map,message);
        return ApiResult.failed(message);
    }

    /**
     * ??????
     * @param dto
     * @return
     */
    @ApiOperation("????????????")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        User user = iUserService.getUserByUsername(dto.getUsername());
       String token = iUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(token)) {
            return ApiResult.failed("??????????????????");
        }

        return userController.loginSuccess(token);
    }

    /**
     * ???????????????log??????
     */
    @OperLog(operModul = "User",operType = "post", operDesc = "????????????")
    public ApiResult<Map<String,String>> loginSuccess(String token){
        Map<String, String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map, "????????????");
    }

    /**
     * ????????????????????????
     * @return
     */
    @ApiOperation("????????????????????????")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<User> getUser() {
        User user = AuthInterceptor.getCurrentUser();
        return ApiResult.success(user);
    }

    /**
     * ??????
     * @return
     */
    @ApiOperation("????????????")
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/logout",method = RequestMethod.PUT)
    public ApiResult<Object> logOut() {
        userController.logOutSuccess();
        return ApiResult.success(null,"????????????") ;
    }

    /**
     * ???????????????log??????
     */
    @OperLog(operModul = "User",operType = "get", operDesc = "????????????")
    public void logOutSuccess(){
    }

    /**
     * ????????????
     * @param username
     * @return
     */
    @ApiOperation("????????????????????????")
    @GetMapping("/search/name/{username}")
    public ApiResult<Map<String, Object>> getUserByName(
            @ApiParam("?????????username")@PathVariable("username")String username) {
        Map<String, Object> map1 = new HashMap<>(16);
        User user = iUserService.getUserByUsername(username);
        if (user == null)
            return ApiResult.failed("???????????????");
        List<Post> posts = iPostService.getBaseMapper().selectList(new LambdaQueryWrapper<Post>().eq(Post::getUserId, user.getId()));
        map1.put("user", user);
        map1.put("posts",posts) ;
        return ApiResult.success(map1);
    }


    /**
     * ??????id?????????
     * @param userid
     * @return
     */
    @ApiOperation("????????????ID?????????")
    @GetMapping("/search/id/{id}")
    public ApiResult<User> getUserById(
            @ApiParam("??????id")@PathVariable("id") String userid) {
        User user = iUserService.getBaseMapper().selectById(userid);
        if (ObjectUtils.isEmpty(user)){
            return ApiResult.failed("???????????????");
        }
        return ApiResult.success(user);
    }

    /**
     * ??????????????????
     * @param user
     * @return
     */
    @ApiOperation("????????????????????????")
    @PostMapping("/update")
    public ApiResult<User> updateUser(@RequestBody User user) {
        iUserService.updateById(user);
        return ApiResult.success(user);
    }

    /**
     * ?????????????????????????????????
     * @return
     */
    @ApiOperation("????????????????????????????????????")
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
     * ??????????????????????????????
     * @return
     */
    @ApiOperation("??????????????????????????????")
    @LoginRequired(allowAll = true)
    @GetMapping("/post/list")
    public ApiResult<List<PostVO>> getAllPosts() throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        List<PostVO> postVOs = iPostService.getAllPostForUser(user.getId());
        return ApiResult.success(postVOs);
    }

    /**
     * ??????????????????
     * @param newAlias ???????????????
     * @return
     */
    @ApiOperation("??????????????????")
    @LoginRequired(allowAll = true)
    @PostMapping("/update/alias")
    public ApiResult<ProfileVO> updateUser(
            @ApiParam("?????????????????????alias??????")@RequestBody Map<String, String> newAlias) {
        System.out.println(newAlias);
        User user = AuthInterceptor.getCurrentUser();
        user.setAlias(newAlias.get("alias"));
        iUserService.getBaseMapper().updateById(user);
        return ApiResult.success(iUserService.getUserProfile(user.getId()));
    }

    /**
     * ??????????????????
     * @param file ?????????????????????
     * @return
     * @throws IOException
     */
    @ApiOperation("??????????????????")
    @LoginRequired(allowAll = true)
    @PostMapping("/update/avatar")
    public ApiResult<ProfileVO> updateUserAvatar(
            @ApiParam("??????????????????") MultipartFile file) throws IOException {
        /*?????????????????????
        accessKey: asn5hMMKnHCgOOnJw-f_0oh1ATDSmm1N7kTHS5jn
        secretKey: A1eMsw3pFjiLLO_cdm7sXF28xydYUp7ExI5PKOLb
        ?????????
        bucket: huangdou
        ?????????
        zone: huanan
        ??????
        domain: qxksa1qbs.hn-bkt.clouddn.com
        */
        // ???????????????
        User user = AuthInterceptor.getCurrentUser();

        String accessKey = "asn5hMMKnHCgOOnJw-f_0oh1ATDSmm1N7kTHS5jn";
        String secretKey = "A1eMsw3pFjiLLO_cdm7sXF28xydYUp7ExI5PKOLb";
        String bucket = "huangdou";
        String domain = "qxksa1qbs.hn-bkt.clouddn.com";
        Configuration configuration = new Configuration(Region.huanan());
        // ???????????????
        UploadManager manager = new UploadManager(configuration);

        // ????????????????????????????????????hash
        String key = null;
        try {
            // ????????????????????????
            byte[] uploadBytes = file.getBytes();
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            // ????????????
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = manager.put(byteInputStream,key,upToken,null, null);
                //???????????????????????????
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
                    return ApiResult.failed("??????????????????");
                }
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return ApiResult.failed("??????????????????");
        }
        return ApiResult.success(iUserService.getUserProfile(user.getId()));
    }

    /**
     * ??????GitLab?????????
     * @return
     */
    @ApiOperation("??????GitLab?????????")
    @LoginRequired(allowAll = true)
    @PostMapping(value = "/bindGitLab")
    public ApiResult<UserGitLab> bindGitLabUsername(
            @ApiParam("??????GitLab?????????")@RequestBody String gitLabUsername
    ) {
        User user = AuthInterceptor.getCurrentUser();
        return ApiResult.success(iUserGitLabService.bindGitLabUsername(user, gitLabUsername));
    }

    @ApiOperation("??????GitLab?????????")
    @LoginRequired(allowAll = true)
    @PostMapping(value = "/unbbindGitLab")
    public ApiResult<Object> unbindGitLabUsername() {
        User user = AuthInterceptor.getCurrentUser();
        String gitLabUsername = iUserGitLabService.getGitLabUsernameByUser(user);
        if (gitLabUsername == null) {
            return ApiResult.failed("?????????");
        }
        if (iUserGitLabService.unbindGitLabUsername(user)) {
            return ApiResult.success("????????????");
        }
        else {
            return ApiResult.failed("????????????");
        }
    }



}