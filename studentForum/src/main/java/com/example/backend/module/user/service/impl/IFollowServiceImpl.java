package com.example.backend.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.user.entity.Follow;
import com.example.backend.module.user.mapper.FollowMapper;
import com.example.backend.module.user.service.IFollowService;
import org.springframework.stereotype.Service;

@Service
public class IFollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

}
