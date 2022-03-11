package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.log.mapper.ExceptionLogMapper;
import com.example.backend.module.log.entity.ExceptionLog;
import com.example.backend.service.IExceptionLogService;
import org.springframework.stereotype.Service;

@Service
public class IExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements IExceptionLogService {

}
