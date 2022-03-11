package com.example.backend.module.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.module.student.entity.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper extends BaseMapper<Student> {
}
