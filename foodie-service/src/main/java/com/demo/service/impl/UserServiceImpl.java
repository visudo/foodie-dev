package com.demo.service.impl;


import com.demo.common.enums.Sex;
import com.demo.common.utils.DateUtil;
import com.demo.common.utils.MD5Utils;
import com.demo.mapper.UsersMapper;
import com.demo.pojo.Users;
import com.demo.pojo.bo.UserBO;
import com.demo.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    public static final String USER_FACE = "https://s1.chu0.com/pvimg/img/png/5f/5f4d95cbaf7c45b78328d9cfe54ee322.png?imageMogr2/auto-orient/thumbnail/!240x240r/gravity/Center/crop/240x240/quality/85/&e=1735488000&token=1srnZGLKZ0Aqlz6dk7yF4SkiYf4eP-YrEOdM1sob:L-DRIftHEDsVF1RCDjlgxH9mjqQ=";

    @Override
    public boolean queryUserNameIsExsit(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);
        return usersMapper.selectOneByExample(userExample) != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        String userId=sid.nextShort();
        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        //默认头像
        user.setFace(USER_FACE);
        //设置默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        //设置默认性别为保密
        user.setSex(Sex.secret.type);
        //设置创建日期
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);
        return user;
    }

    /**
     * 检索用户名和密码是否匹配，用于登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {
        Example userExample=new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);
        userCriteria.andEqualTo("password",password);

        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }
}
