package com.demo.controller;

import com.demo.common.utils.CookieUtils;
import com.demo.common.utils.DemoJsonResult;
import com.demo.common.utils.JsonUtils;
import com.demo.common.utils.MD5Utils;
import com.demo.pojo.Users;
import com.demo.pojo.bo.UserBO;
import com.demo.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RestController
@RequestMapping("/passport")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/usernameIsExist")
    public DemoJsonResult getUserByUsername(@RequestParam String username) {
        if (StringUtils.isEmpty(username)) {
            return DemoJsonResult.errorMsg("username not empty");
        }
        if (userService.queryUserNameIsExsit(username)) {
            return DemoJsonResult.ok();
        }
        return DemoJsonResult.errorMsg("username not empty");
    }

    @PostMapping("/regist")
    public DemoJsonResult regist(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return DemoJsonResult.errorMsg("用户名不能为空");
        }
        //判断用户名是否存在
        boolean isExist = userService.queryUserNameIsExsit(username);
        if (isExist) {
            return DemoJsonResult.errorMsg("用户名已存在");
        }
        //判断密码长度不少于6位
        if (password.length() < 6) {
            return DemoJsonResult.errorMsg("密码长度不能少于6位");
        }
        //判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return DemoJsonResult.errorMsg("两次密码输入不一致");
        }

        //实现注册
        Users userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);

        //将用户信息加密存储到cookie中
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        return DemoJsonResult.ok();

    }

    @PostMapping("/login")
    public DemoJsonResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        //判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return DemoJsonResult.errorMsg("用户名或密码不能为空");
        }

        //实现实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null){
            return DemoJsonResult.errorMsg("用户名或密码不正确");
        }

        userResult =  setNullProperty(userResult);

        //将用户信息加密存储到cookie中
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(userResult),true);

        //TODO 生成用户token,存入redis回话
        //TODO 同步购物车数据

        return DemoJsonResult.ok(userResult);

    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    @PostMapping("/logout")
    public DemoJsonResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        //清楚用户相关信息的cookie
        CookieUtils.deleteCookie(request,response,"user");

        //TODO 用户退出需要清楚购物车
        //TODO 分布式会话中

        return DemoJsonResult.ok();

    }

}
