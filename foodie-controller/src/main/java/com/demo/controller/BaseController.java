package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.demo.common.utils.DemoJsonResult;
import com.demo.pojo.Orders;
import com.demo.service.center.MyOrdersService;

@Controller
public class BaseController {

    public static final Integer COMMON_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    // 支付中心地址
    String paymentUrl = "http://127.0.0.1:8088/foodie-payment/payment/createMerchantOrder";        // produce

    // 支付中心的调用地址
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    @Autowired
    public MyOrdersService myOrdersService;
    //校验用户和订单信息是否关联
    public DemoJsonResult checkUserOrder(String userId, String orderId){
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order==null){
            return DemoJsonResult.errorMsg("订单不存在");
        }
        return DemoJsonResult.ok(order);

    }
}
