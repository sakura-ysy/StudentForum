package com.example.backend.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.module.user.dto.LoginDTO;
import com.example.backend.module.user.dto.RegisterDTO;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.vo.ProfileVO;

public interface IUserService extends IService<User> {

    /**
     * 注册功能
     *
     * @param dto
     * @return 注册对象
     */
    User executeRegister(RegisterDTO dto);
    /**
     * 获取用户信息
     *
     * @param username
     * @return dbUser
     */
    User getUserByUsername(String username);
    /**
     * 用户登录
     *
     * @param dto
     * @return 生成的JWT的token
     */
    String executeLogin(LoginDTO dto);
    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return
     */
    ProfileVO getUserProfile(String id);
}
