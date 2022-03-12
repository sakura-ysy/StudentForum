package com.example.backend.module.teacher.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.teacher.entity.Teacher;
import com.example.backend.module.teacher.mapper.TeacherMapper;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.impl.IUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "只允许教师访问的接口")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private IUserServiceImpl iUserService;

    @ApiOperation("获取该教师的信息")
    @LoginRequired(allowTeacher = true)
    @GetMapping(value = "/info")
    public ApiResult<Teacher> getTeacherInfo(){
        User user = AuthInterceptor.getCurrentUser();
        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserName, user.getUsername()));
        if (ObjectUtils.isEmpty(teacher)){
            return ApiResult.failed("不是教师");
        }
        return ApiResult.success(teacher);
    }

    @ApiOperation("更新该教师信息")
    @LoginRequired(allowTeacher = true)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<Teacher> updateTeacherInfo(@RequestBody Teacher teacher){
        User user = AuthInterceptor.getCurrentUser();
        Teacher oldInfo = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserName, user.getUsername()));
        if (ObjectUtils.isEmpty(oldInfo)){
            return ApiResult.failed("不是教师");
        }
        teacher.setId(oldInfo.getId());
        teacher.setUserId(oldInfo.getUserId());
        teacher.setUserName(oldInfo.getUserName());
        teacherMapper.updateById(teacher);
        return ApiResult.success(teacher);
    }
}
