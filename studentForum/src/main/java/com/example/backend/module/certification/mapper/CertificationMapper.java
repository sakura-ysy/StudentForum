package com.example.backend.module.certification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.module.certification.entity.Certification;
import com.example.backend.module.certification.vo.CertificationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationMapper extends BaseMapper<Certification> {
    /**
     * 分页查询列表
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<CertificationVO> selectListAndPage(@Param("page") Page<Certification> page, @Param("tab") String tab);
}
