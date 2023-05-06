package com.demo.service;

import com.demo.pojo.Users;
import com.demo.pojo.bo.UserBO;

public interface UserService {

    // 通过用户名判断用户是否存在
    public boolean queryUserNameIsExsit(String username);

    public Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username,String password);
}
