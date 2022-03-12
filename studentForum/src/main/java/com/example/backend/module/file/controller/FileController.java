package com.example.backend.module.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.file.entity.FileUser;
import com.example.backend.module.file.mapper.FileUserMapper;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "文件相关接口")
@RestController
@RequestMapping("/api/file")
public class FileController {
    @Resource
    private IUserService iUserService;
    @Resource
    private FileUserMapper fileUserMapper;

    @ApiOperation("上传文件")
    @LoginRequired(allowAll = true)
    @PostMapping("/upload")
    public ApiResult<List<String>> fileUpload(MultipartFile[] file) throws IOException {
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
        List<String> list = new ArrayList<>();
        try {
            for (MultipartFile oneFile : file) {

                // 文件转化为字节流
                byte[] uploadBytes = oneFile.getBytes();
                ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
                // 上传凭证
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);
                try {
                    Response response = manager.put(byteInputStream,key,upToken,null, null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    System.out.println(putRet.key);
                    System.out.println(putRet.hash);
                    FileUser addFileUser = FileUser.builder()
                            .url(domain + "/" + putRet.key)
                            .userId(user.getId())
                            .username(user.getUsername())
                            .build();
                    fileUserMapper.insert(addFileUser);
                    list.add(domain + "/" + putRet.key);
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
            }

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return ApiResult.failed("上传文件成功");
        }
        return ApiResult.success(list,"上传成功");
    }

    @ApiOperation("获取当前用户上传的全部文件URL")
    @LoginRequired(allowAll = true)
    @GetMapping(value = "/list")
    public ApiResult<List<String>> getFilesOfUser(){
        User user = AuthInterceptor.getCurrentUser();
        List<FileUser> fileList = fileUserMapper.selectList(new LambdaQueryWrapper<FileUser>().eq(FileUser::getUsername, user.getUsername()));
        List<String> list = new ArrayList<>();
        for (FileUser fileUser : fileList) {
            list.add(fileUser.getUrl());
        }
        return ApiResult.success(list);
    }

}
