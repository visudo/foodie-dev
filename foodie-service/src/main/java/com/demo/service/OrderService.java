package com.demo.service;

import com.demo.pojo.OrderStatus;
import com.demo.pojo.bo.SubmitOrderBO;
import com.demo.pojo.vo.OrderVO;

public interface OrderService {

    /**
     * 创建一个订单
     *
     * @param submitOrderBO
     */
    OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
     OrderStatus queryOrderStatusInfo(String orderId) ;

    /**
     * 关闭超时未支付订单
     */
    void closeOrder();
}
