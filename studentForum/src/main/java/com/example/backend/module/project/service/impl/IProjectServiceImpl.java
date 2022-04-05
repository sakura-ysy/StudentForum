package com.example.backend.module.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.api.ErrorResponse;
import com.example.backend.module.project.dto.ViewProjectDTO;
import com.example.backend.module.project.dto.ViewProjectUserDTO;
import com.example.backend.module.project.entity.Project;
import com.example.backend.module.project.entity.ProjectGitLabUser;
import com.example.backend.module.project.mapper.ProjectGitLabUserMapper;
import com.example.backend.module.project.mapper.ProjectMapper;
import com.example.backend.module.project.service.IProjectService;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.entity.UserGitLab;
import com.example.backend.module.user.mapper.UserGitLabMapper;
import com.example.backend.module.user.service.IUserGitLabService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.backend.common.api.ApiErrorCode.POST_NOT_EXISTS;

@Service
public class IProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
    @Resource
    IUserGitLabService iUserGitLabService;

    @Resource
    ProjectGitLabUserMapper projectGitLabUserMapper;
    @Resource
    UserGitLabMapper userGitLabMapper;


    @Override
    public Page<ViewProjectDTO> getProjectPage(Page<Project> page, String tab) throws IOException {
        // 项目分页列表
        Page<Project> iPage = this.baseMapper.selectProjectPage(page, tab);
        Page<ViewProjectDTO> resPage = new Page<>(page.getCurrent(), page.getSize());
        List<ViewProjectDTO> viewProjectDTOS = new ArrayList<>();
        for (Project project : iPage.getRecords()) {
            viewProjectDTOS.add(changeProjectToProjectDTO(project));
        }
        resPage.setRecords(viewProjectDTOS);
        resPage.setTotal(page.getTotal());
        return resPage;
    }

    @Override
    public Map<String, Object> getProjectInfo(String id) throws IOException {
        Map<String, Object> map = new HashMap<>(16);
        Project project = this.baseMapper.selectById(id);
        if (project == null){
            ErrorResponse.sendJsonMessage(POST_NOT_EXISTS);
            return null;
        }
        Map<String,Object> projectMap = JSONObject.parseObject(JSON.toJSONString(project));
        map.put("project", projectMap);

        List<ViewProjectUserDTO> userMap = null;
        List<ProjectGitLabUser> projectGitLabUsers= projectGitLabUserMapper.selectList(new LambdaQueryWrapper<ProjectGitLabUser>().
                eq(ProjectGitLabUser::getProjectId,id));
        for (ProjectGitLabUser projectGitLabUser: projectGitLabUsers) {
            String tempGitLabUsername = projectGitLabUser.getGitLabUsername();
            UserGitLab userGitLab = userGitLabMapper.selectOne(new LambdaQueryWrapper<UserGitLab>()
                    .eq(UserGitLab::getGitLabUsername, tempGitLabUsername));
            userMap.add(ViewProjectUserDTO.builder()
                    .gitLabUsername(tempGitLabUsername)
                    .userId(userGitLab.getUserId())
                    .username(userGitLab.getUsername())
                    .build());
        }

        map.put("user", userMap);
        return map;
    }

    @Override
    public ViewProjectDTO bindProject(String projectId, User user, int projectRole) throws IOException {
        // todo 插入一对多表
        Project project = baseMapper.selectById(projectId);
        ProjectGitLabUser projectGitLabUser = ProjectGitLabUser.builder()
                .gitLabUsername(iUserGitLabService.getGitLabUsernameByUser(user))
                .projectId(projectId)
                .projectName(project.getName())
                .projectRole(projectRole)
                .build();
        projectGitLabUserMapper.insert(projectGitLabUser);
        return changeProjectToProjectDTO(baseMapper.selectById(projectId));
    }

    @Override
    public Boolean unbindProject(String id, User user) throws IOException {
        // todo 从一对多表删除
        return null;
    }

    @Override
    public ViewProjectDTO changeProjectToProjectDTO(Project project) throws IOException {
        ViewProjectDTO viewProjectDTO = new ViewProjectDTO();
        BeanUtils.copyProperties(project,viewProjectDTO);
        return viewProjectDTO;
    }
}
