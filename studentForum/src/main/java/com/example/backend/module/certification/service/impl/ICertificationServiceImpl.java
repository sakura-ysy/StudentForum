package com.example.backend.module.certification.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.certification.dto.CertificationDTO;
import com.example.backend.module.certification.entity.Certification;
import com.example.backend.module.certification.mapper.CertificationMapper;
import com.example.backend.module.certification.service.ICertificationService;
import com.example.backend.module.certification.vo.CertificationVO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.IUserService;
import com.example.backend.module.user.vo.ProfileVO;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ICertificationServiceImpl extends ServiceImpl<CertificationMapper, Certification> implements ICertificationService {
    @Resource
    private CertificationMapper certificationMapper;
    @Autowired
    private IUserService iUserService;

    @Override
    public Page<CertificationVO> getList(Page<Certification> page, String tab) {
        // 查询认证
        Page<CertificationVO> iPage = this.baseMapper.selectListAndPage(page, tab);

        iPage.getRecords().forEach(certificationVO -> {
            certificationVO.setFileList(JSONObject.parse(certificationVO.getFiles()));
        });
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Certification create(CertificationDTO dto, User user, List<String> fileList) {
        // 查询认证是否已经存在
        Certification certification1 = this.baseMapper.selectOne(
                new LambdaQueryWrapper<Certification>()
                        .eq(Certification::getAchievement, dto.getAchievement())
                        .eq(Certification::getUserId, user.getId()
                        )
        );
        Assert.isNull(certification1, "该认证已存在，请修改");
        System.out.println(fileList);

        // 不存在则创建
        Certification certification = Certification.builder()
                .achievement(dto.getAchievement())
                .keyWord(dto.getKeyWord())
                .content(dto.getContent())
                .numLimit(dto.getNumLimit())
                .userId(user.getId())
                .createTime(new Date())
                .files(JSONObject.toJSONString(fileList))
                .build();

        // tags
        String tags = "";
        for (String tag : dto.getTags()) {
            tags = tags + tag + ";";
        }
        certification.setTags(tags);
        this.baseMapper.insert(certification);
        return null;
    }

    @Override
    public Map<String, Object> viewCertification(String id) {
        Map<String, Object> map = new HashMap<>(16);
        Certification certification = this.baseMapper.selectById(id);
        Assert.notNull(certification, "当前认证不存在，或者被申请者删除");

        // emoji转码
        certification.setContent(EmojiParser.parseToUnicode(certification.getContent()));
        map.put("certification", certification);
        // 作者
        ProfileVO user = iUserService.getUserProfile(certification.getUserId()); // 得到用户信息，只需要Profile中的字段
        map.put("user", user);

        return map;
    }

}
