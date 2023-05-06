package com.demo.controller;


import com.demo.common.utils.DemoJsonResult;
import com.demo.pojo.bo.ShopcartBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("shopcart")
public class ShopcartController {

    @PostMapping("/add")
    public DemoJsonResult add(@RequestParam String userId,
                              @RequestBody ShopcartBO shopcartBO,
                              HttpServletRequest request,
                              HttpServletResponse response){

        if (StringUtils.isBlank(userId)){
            return DemoJsonResult.errorMsg("");
        }

        //TODO 前端用户登录的情况下，添加商品到购物车，会同时在后端同步到redis中。此时购物车数据由前端存储在Cookie中

        return DemoJsonResult.ok();

    }


    @PostMapping("/del")
    public DemoJsonResult del(@RequestParam String userId,
                               @RequestBody String itemSpecId,
                               HttpServletRequest request,
                               HttpServletResponse response){

        if (StringUtils.isBlank(userId)|| StringUtils.isBlank(itemSpecId)){
            return DemoJsonResult.errorMsg("");
        }

        //TODO 用户在页面删除购物车中的商品数据，如果此时用户已登录需要同步删除后端购物车中数据

        return DemoJsonResult.ok();

    }



}
