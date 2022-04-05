package com.example.backend.module.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.project.dto.ViewProjectDTO;
import com.example.backend.module.project.entity.Project;
import com.example.backend.module.user.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IProjectService extends IService<Project> {
    /**
     * 获取项目列表
     * @param page
     * @param tab
     * @return
     */
    Page<ViewProjectDTO> getProjectPage(Page<Project> page, String tab) throws IOException;

    /**
     * 获取项目详情
     * @param id
     * @return
     * @throws IOException
     */

    /**
     * 项目详情
     * @param id
     * @return
     */
    Map<String, Object> getProjectInfo(String id) throws IOException;

    /**
     * 绑定项目
     * @param projectId
     * @param user
     * @return
     */
    ViewProjectDTO bindProject(String projectId, User user, int projectRole) throws IOException;

    /**
     * 取消绑定
     * @param id
     * @param user
     * @return
     */
    Boolean unbindProject(String id, User user) throws IOException;

    /**
     * 转换DTO
     * @param project
     * @return
     */
    ViewProjectDTO changeProjectToProjectDTO(Project project) throws IOException;

}
