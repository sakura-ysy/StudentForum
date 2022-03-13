package com.example.backend.module.certification.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.certification.dto.CertificationDTO;
import com.example.backend.module.certification.entity.Certification;
import com.example.backend.module.certification.entity.CertificationVote;
import com.example.backend.module.certification.mapper.CertificationMapper;
import com.example.backend.module.certification.mapper.CertificationVoteMapper;
import com.example.backend.module.certification.service.ICertificationService;
import com.example.backend.module.certification.vo.CertificationVO;
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
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "认证相关接口, 只是个模拟, 先不用")
@RestController
@RequestMapping("/api/certification")
public class CertificationController {
    @Resource
    private ICertificationService iCertificationService;
    @Resource
    private IUserService umsUserService;

    @Resource
    private CertificationMapper certificationMapper;

    @Resource
    private CertificationVoteMapper certificationVoteMapper;

    /**
     * 返回所有认证需求
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ApiResult<Page<CertificationVO>> list(@RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                               @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<CertificationVO> list = iCertificationService.getList(new Page<>(pageNo, pageSize), "latest");
        // Page<> 是自带的有关处理分页的类
        return ApiResult.success(list);
    }

    /**
     * 发布认证需求
     * @param dto
     * @return
     */
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<Certification> create(@RequestPart("files") MultipartFile[] files, CertificationDTO dto) throws IOException {
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
        List<String> fileList = new ArrayList<>();
        try {
            for (MultipartFile oneFile : files) {

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
                    String fileUrl = domain + "/" + putRet.key;
                    fileList.add(fileUrl);
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

        Certification certification = iCertificationService.create(dto, user,fileList);
        return ApiResult.success(certification);
    }


    /**
     * 获取认证详情
     * @param id
     * @return
     */
    @LoginRequired(allowAll = true)
    @GetMapping("/{id}")
    public ApiResult<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String, Object> map = iCertificationService.viewCertification(id);
        return ApiResult.success(map);
    }

    /**
     * 删除认证
     * @param id 删除认证的id
     * @return 是否成功
     */
    @LoginRequired(allowAll = true)
    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(@PathVariable("id") String id) {
        User user = AuthInterceptor.getCurrentUser();
        Certification byId = iCertificationService.getById(id);
        Assert.notNull(byId, "该话题已删除");
        // 判断待删除帖子的作者id是否等于当前用户id，如果不等于则抛出message，结束
        Assert.isTrue(byId.getUserId().equals(user.getId()), "无法删除别人的帖子");
        // 删除
        iCertificationService.removeById(id);
        return ApiResult.success(null,"删除成功");
    }

    /**
     * 获得赞同票数
     * @param certificationId
     * @return 赞同票数
     */
    @GetMapping(value = "/agree/{id}")
    public ApiResult<Integer> agreeNumber(@PathVariable("id") String certificationId){
        Certification certification = certificationMapper.selectById(certificationId);
        if (ObjectUtils.isEmpty(certification))
            return ApiResult.failed("帖子不存在");
        return ApiResult.success(certification.getAgree());
    }

    /**
     * 获取反对票数
     * @param certificationId
     * @return
     */
    @GetMapping(value = "/disagree/{id}")
    public ApiResult<Integer> disagreeNumber(@PathVariable("id") String certificationId){
        Certification certification = certificationMapper.selectById(certificationId);
        if (ObjectUtils.isEmpty(certification))
            return ApiResult.failed("帖子不存在");
        return ApiResult.success(certification.getDisagree());
    }

    /**
     * 投票
     * @param certificationId
     * @param isAgree
     * @return
     */
    @LoginRequired(allowAll = true)
    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public ApiResult<String> vote(@RequestParam("certificationId") String certificationId,
                                  @RequestParam("isAgree") Integer isAgree) {
        Certification certification = certificationMapper.selectById(certificationId);
        User user = AuthInterceptor.getCurrentUser();
        if (ObjectUtils.isEmpty(certification))
            return ApiResult.failed("成就认证任务不存在");

        if(certification.getUserId().equals(user.getId()))
            return ApiResult.failed("不能给自己投票");
        CertificationVote oldCertificationVote = certificationVoteMapper.selectOne(new LambdaQueryWrapper<CertificationVote>()
                .eq(CertificationVote::getCertificationId, certificationId)
                .eq(CertificationVote::getVoterId, user.getId()));
        if(!ObjectUtils.isEmpty(oldCertificationVote))
            return ApiResult.failed("不能重复投票");

        if(certification.getNumSum().equals(certification.getNumLimit()))
            return ApiResult.failed("投票已满，该认证停止投票");

        certification.setNumSum(certification.getNumSum() + 1);

        if(isAgree == 1) {
            certification.setAgree(certification.getAgree() + 1);
            CertificationVote newCertificationVote = CertificationVote.builder()
                    .certificationId(certificationId)
                    .voterId(user.getId())
                    .isAgree(true)
                    .build();
            certificationVoteMapper.insert(newCertificationVote);
            certificationMapper.updateById(certification);
            return ApiResult.success("同意");
        } else if (isAgree == 0) {
            certification.setDisagree(certification.getDisagree() + 1);
            CertificationVote newCertificationVote = CertificationVote.builder()
                    .certificationId(certificationId)
                    .voterId(user.getId())
                    .isAgree(false)
                    .build();
            certificationVoteMapper.insert(newCertificationVote);
            certificationMapper.updateById(certification);
            return ApiResult.success("不同意");
        } else return ApiResult.success("投票接口异常");
    }
}
