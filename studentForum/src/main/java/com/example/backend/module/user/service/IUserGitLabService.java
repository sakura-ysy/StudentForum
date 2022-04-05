package com.example.backend.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.entity.UserGitLab;

public interface IUserGitLabService extends IService<UserGitLab> {

    String getGitLabUsernameByUser(User user);

    User getUserByGitLabUsername(String gitLabUsername);

    UserGitLab bindGitLabUsername(User user, String gitLabUserName);

    Boolean unbindGitLabUsername(User user);
}
