package com.demo.service.center;

import com.demo.common.utils.PagedGridResult;
import com.demo.pojo.Orders;
import com.demo.pojo.vo.OrderStatusCountsVO;

public interface MyOrdersService {

    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId,
                                         Integer orderStatus,
                                         Integer page,
                                         Integer pageSize);

    /**
     * 订单状态 --》 商家发货
     * @param orderId
     */
    public void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单
     * @param userId
     * @param orderId
     * @return
     */
    public Orders queryMyOrder(String userId, String orderId );

    /**
     * 更新订单状态 --》确认收货
     * @return
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单（逻辑删除）
     * @param userId
     * @param orderId
     * @return
     */
    public boolean deleteOrder(String userId,String orderId);

    /**
     * 查询用户订单数
     * @param userId
     */
    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult getMyOrderTrend(String userId,Integer page,Integer pageSize);


}
