package com.example.backend.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.entity.UserGitLab;
import com.example.backend.module.user.mapper.UserGitLabMapper;
import com.example.backend.module.user.mapper.UserMapper;
import com.example.backend.module.user.service.IUserGitLabService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IUserGitLabServiceImpl extends ServiceImpl<UserGitLabMapper, UserGitLab> implements IUserGitLabService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String getGitLabUsernameByUser(User user) {
        return baseMapper.selectOne(new LambdaQueryWrapper<UserGitLab>().eq(UserGitLab::getUserId, user.getId())).getGitLabUsername();

    }

    @Override
    public User getUserByGitLabUsername(String gitLabUsername) {
        String userId = baseMapper.selectOne(new LambdaQueryWrapper<UserGitLab>().eq(UserGitLab::getGitLabUsername, gitLabUsername)).getUserId();
        return userMapper.selectById(userId);
    }

    @Override
    public UserGitLab bindGitLabUsername(User user, String gitLabUserName) {
        UserGitLab userGitLab = UserGitLab.builder()
                .username(user.getUsername())
                .gitLabUsername(gitLabUserName)
                .build();
        baseMapper.insert(userGitLab);
        return baseMapper.selectById(user.getId());
    }

    @Override
    public Boolean unbindGitLabUsername(User user) {
        int res = baseMapper.delete(new LambdaQueryWrapper<UserGitLab>().eq(UserGitLab::getUserId, user.getId()));
        return res != 0;
    }
}
