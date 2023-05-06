package com.demo.controller;


import com.demo.common.enums.OrderStatusEnum;
import com.demo.common.enums.PayMethod;
import com.demo.common.utils.CookieUtils;
import com.demo.common.utils.DemoJsonResult;
import com.demo.pojo.bo.SubmitOrderBO;
import com.demo.pojo.vo.MerchantOrdersVO;
import com.demo.pojo.vo.OrderVO;
import com.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/create")
    public DemoJsonResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response){


        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type &&
        submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type){
            return DemoJsonResult.errorMsg("支付方式不正确");
        }
        //1、创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        //2、移除购物车中已提交(已结算)的商品
        //TODO 整合redis之后，完善购物车中的已结算商品清楚，并且同步到前端的cookie
        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);



        //3、向支付中心发送当前订单，用于保存支付中心的订单
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        //为方便测试，所有的订单金额都改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","10010");
        headers.add("password","123456");

        HttpEntity<MerchantOrdersVO> entity=new HttpEntity<>(merchantOrdersVO,headers);
        ResponseEntity<DemoJsonResult> responseEntity =
                restTemplate.postForEntity(paymentUrl, entity, DemoJsonResult.class);

        DemoJsonResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus()!=200){
            return DemoJsonResult.errorMsg("支付中心订单创建失败，请联系管理员");
        }
        return DemoJsonResult.ok(orderId);
    }

    /**
     * 提供给支付中心，商品支付成功回调接口
     * @param map
     * @return
     */
    @PostMapping("/notifyMerchantOrderPaid")
    public DemoJsonResult notifyMerchantOrderPaid(@RequestBody Map<String,String> map){
        orderService.updateOrderStatus(map.get("merchantOrderId"), OrderStatusEnum.WAIT_DELIVER.type);
        return DemoJsonResult.ok();
    }

//    @PostMapping("/getPaidOrderInfo")
//    public DemoJsonResult getPaidOrderInfo(String orderId){
//
//        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
//        return DemoJsonResult.ok(orderStatus);
//
//    }
}
