package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.log.mapper.OperationLogMapper;
import com.example.backend.module.log.entity.OperationLog;
import com.example.backend.service.IOperationLogService;
import org.springframework.stereotype.Service;

@Service
public class IOperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {
}