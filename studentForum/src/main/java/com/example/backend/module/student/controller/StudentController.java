package com.example.backend.module.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.annotation.LoginRequired;
import com.example.backend.common.api.ApiResult;
import com.example.backend.jwt.AuthInterceptor;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.student.entity.Student;
import com.example.backend.module.student.mapper.StudentMapper;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.service.impl.IUserServiceImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private IUserServiceImpl iUserService;

    @LoginRequired(allowStudent = true)
    @RequestMapping("/info")
    public ApiResult<Student> getStudentInfo(){
        User user = AuthInterceptor.getCurrentUser();
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserName, user.getUsername()));
        if (ObjectUtils.isEmpty(student)){
            return ApiResult.failed("不是学生");
        }
        return ApiResult.success(student);
    }

    @LoginRequired(allowStudent = true)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<Student> updateStudentInfo(@RequestBody Student student){
        User user = AuthInterceptor.getCurrentUser();
        Student oldInfo = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserName, user.getUsername()));
        if (ObjectUtils.isEmpty(oldInfo)){
            return ApiResult.failed("不是学生");
        }
        student.setId(oldInfo.getId());
        student.setUserId(oldInfo.getUserId());
        student.setUserName(oldInfo.getUserName());
        studentMapper.updateById(student);
        return ApiResult.success(student);
    }
}
