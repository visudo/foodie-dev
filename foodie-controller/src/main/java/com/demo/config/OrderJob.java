package com.demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.demo.service.OrderService;

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务关闭超期未支付订单，会存在的弊端
     * 1、会有时间差，程序不严谨
     * 2、不支持集群
     * 3、会对数据库全表搜索
     */

    //@Scheduled(cron = "0/3 * * * * ?")
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder(){
        orderService.closeOrder();
    }
}
