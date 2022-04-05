package com.example.backend.module.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.module.project.dto.ViewProjectDTO;
import com.example.backend.module.project.entity.Project;
import com.example.backend.module.project.service.IProjectService;
import com.example.backend.module.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Api(tags = "项目相关接口")
@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Resource
    private IProjectService iProjectService;

    @ApiOperation("分页获取问题列表")
    @GetMapping("/list")
    public ApiResult<Page<ViewProjectDTO>> getProjectPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "tab", defaultValue = "latest") String tab) throws IOException {
        Page<Project> page = new Page<>(pageNo,pageSize);
        Page<ViewProjectDTO> res = iProjectService.getProjectPage(page,tab);
        return ApiResult.success(res);
    }

    @ApiOperation("项目详情")
    @GetMapping("/{id}")
    public ApiResult<Map<String, Object>> getInfo(
            @ApiParam("项目id") @PathVariable("id") String id) throws IOException {
        Map<String, Object> map = iProjectService.getProjectInfo(id);
        return ApiResult.success(map);
    }

    @ApiOperation("绑定项目")
    @LoginRequired(allowAll = true)
    @PostMapping("/bind")
    public ApiResult<ViewProjectDTO> createProject(@RequestBody String projectId,
                                                   @RequestBody int projectRole) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        ViewProjectDTO res = iProjectService.bindProject(projectId, user, projectRole);
        return ApiResult.success(res);
    }

    @ApiOperation("取消绑定项目")
    @LoginRequired(allowAll = true)
    @DeleteMapping("/unbind")
    public ApiResult<Object> unbindProject(@RequestParam("id") String id) throws IOException {
        User user = AuthInterceptor.getCurrentUser();
        Boolean res = iProjectService.unbindProject(id, user);
        if (!res)
            return ApiResult.failed("撤销失败");
        return ApiResult.success("撤销成功");
    }
}
