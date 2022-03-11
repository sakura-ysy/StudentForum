package com.example.backend.module.certification.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.certification.dto.CertificationDTO;
import com.example.backend.module.certification.entity.Certification;
import com.example.backend.module.certification.vo.CertificationVO;
import com.example.backend.module.user.entity.User;

import java.util.List;
import java.util.Map;

public interface ICertificationService extends IService<Certification> {
    /**
     * 获取认证任务列表
     *
     * @param page
     * @param tab
     * @return
     */
    Page<CertificationVO> getList(Page<Certification> page, String tab);

    /**
     * 发布
     * @param dto
     * @return
     */
    Certification create(CertificationDTO dto, User user, List<String> fileList);

    /**
     * 查看认证任务详情
     *
     * @param id
     * @return
     */
    Map<String, Object> viewCertification(String id);

    /**
     * 投票
     * @param id
     * @param isAgree
     */
}
