package com.example.backend.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.jwt.JwtUtils;
import com.example.backend.module.company.entity.Company;
import com.example.backend.module.company.mapper.CompanyMapper;
import com.example.backend.module.post.entity.Post;
import com.example.backend.module.post.mapper.TopicMapper;
import com.example.backend.module.student.entity.Student;
import com.example.backend.module.student.mapper.StudentMapper;
import com.example.backend.module.teacher.entity.Teacher;
import com.example.backend.module.teacher.mapper.TeacherMapper;
import com.example.backend.module.user.dto.LoginDTO;
import com.example.backend.module.user.dto.RegisterDTO;
import com.example.backend.module.user.entity.Follow;
import com.example.backend.module.user.entity.User;
import com.example.backend.module.user.mapper.FollowMapper;
import com.example.backend.module.user.mapper.UserMapper;
import com.example.backend.module.user.service.IUserService;
import com.example.backend.module.user.vo.ProfileVO;
import com.example.backend.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j  // 日志
@Service  // 标记当前类是一个service类，加上该注解会将当前类自动注入到spring容器中，不需要再在applicationContext.xml文件定义bean了
@Transactional(rollbackFor = Exception.class)
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private TopicMapper topicMapper;
    @Resource
    private FollowMapper followMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private JwtUtils jwtUtils;

    // 1. 用户注册
    @Override
    public User executeRegister(RegisterDTO dto) {
        //查询是否有相同用户名的用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getName()).or().eq(User::getEmail, dto.getEmail());  // 账号已存在或邮箱已存在
        User user = baseMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(user)) {
            return null ;
        }
        User addUser = User.builder()
                .username(dto.getName())
                .alias(dto.getName())
                .password(MD5Utils.getPwd(dto.getPass()))
                .email(dto.getEmail())
                .roleId(dto.getRole())
                .mobile(dto.getMobile())
                .bio(dto.getBio())
                .createTime(new Date())
                .status(true)
                .build();
        baseMapper.insert(addUser);

        // 插入对应的角色表 todo
        if (dto.getRole() == 1){
            Student addStudent = Student.builder().userId(addUser.getId()).userName(dto.getName()).build();
            studentMapper.insert(addStudent);
        }
        else if (dto.getRole() == 2){
            Teacher addTeacher = Teacher.builder().userId(addUser.getId()).userName(dto.getName()).build();
            teacherMapper.insert(addTeacher);
        }
        else if (dto.getRole() == 3){
            Company addCompany = Company.builder().userId(addUser.getId()).userName(dto.getName()).build();
            companyMapper.insert(addCompany);
        }

        return addUser;
    }

    // 2. 通过用户名获取用户
    @Override
    public User getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    /**
     * /user/login
     * @param dto
     * @return
     */
    @Override
    public String executeLogin(LoginDTO dto) {
        String token = null;
        try {
            User user = getUserByUsername(dto.getUsername());  // 获取输入的用户名
            String encodePwd = MD5Utils.getPwd(dto.getPassword());  // 获取输入的密码并MD5加密
            if(!encodePwd.equals(user.getPassword()))
            {
                throw new Exception("密码错误");
            }
             // 通过id生成token
            token = jwtUtils.generateJwtToken(String.valueOf(user.getId()));
        } catch (Exception e) {
            log.warn("用户不存在or密码验证失败=======>{}", dto.getUsername());
        }
        return token;
    }

    // 4.通过用户id获取用户信息
    @Override
    public ProfileVO getUserProfile(String id) {
        ProfileVO profile = new ProfileVO();
        User user = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(user)){
            return null;
        }
        BeanUtils.copyProperties(user, profile);  // 把user中的同名字段值赋给profile
        // 用户文章数
        int count = topicMapper.selectCount(new LambdaQueryWrapper<Post>().eq(Post::getUserId, id));
        profile.setTopicCount(count);

        // 粉丝数
        int followers = followMapper.selectCount((new LambdaQueryWrapper<Follow>().eq(Follow::getParentId, id)));
        profile.setFollowerCount(followers);

        return profile;
    }
}