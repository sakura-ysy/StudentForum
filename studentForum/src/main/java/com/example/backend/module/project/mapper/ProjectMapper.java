package com.example.backend.module.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.module.project.entity.Project;
import org.apache.ibatis.annotations.Param;

public interface ProjectMapper extends BaseMapper<Project> {
    Page<Project> selectProjectPage(@Param("page") Page<Project> page, @Param("tab") String tab);
}
