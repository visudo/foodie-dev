package com.demo.controller.center;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.common.utils.DemoJsonResult;
import com.demo.pojo.Users;
import com.demo.service.center.CenterUserService;

@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @GetMapping("/userInfo")
    public DemoJsonResult userInfo(@RequestParam String userId){

        Users user = centerUserService.queryUserInfo(userId);
        return DemoJsonResult.ok(user);
    }
}
