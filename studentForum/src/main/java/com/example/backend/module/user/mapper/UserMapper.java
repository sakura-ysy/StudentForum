package com.example.backend.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.module.user.entity.Permission;
import com.example.backend.module.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户
 *
 * @author Knox 2020/11/7
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取用户所有权限
     * @param userName
     * @return
     */
    List<Permission> getPermissionByUsername(String userName);
}
