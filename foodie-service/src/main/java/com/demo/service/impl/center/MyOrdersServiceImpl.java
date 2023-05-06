package com.demo.service.impl.center;


import com.demo.common.enums.OrderStatusEnum;
import com.demo.common.enums.YesOrNo;
import com.demo.common.utils.PagedGridResult;
import com.demo.mapper.OrderStatusMapper;
import com.demo.mapper.OrdersMapper;
import com.demo.mapper.OrdersMapperCustom;
import com.demo.pojo.OrderStatus;
import com.demo.pojo.Orders;
import com.demo.pojo.vo.MyOrdersVO;
import com.demo.pojo.vo.OrderStatusCountsVO;
import com.demo.service.center.MyOrdersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    @Autowired
    private OrdersMapperCustom ordersMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);

        return setterPagedGrid(list, page);
    }

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectOne(orders);
    }

    /**
     * 查询用户订单数
     *
     * @param userId
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("orderStatus",OrderStatusEnum.WAIT_PAY.type);
        int waitPayCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment",YesOrNo.NO.type);
        int waitCommentCount = ordersMapperCustom.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCount,waitDeliverCount,waitReceiveCount,waitCommentCount);

        return countsVO;
    }

    /**
     * 订单状态 --》 商家发货
     *
     * @param orderId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateOrder, example);
    }

    /**
     * 更新订单状态 --》确认收货
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateReceiveOrderStatus( String orderId) {

        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        int result = orderStatusMapper.updateByExampleSelective(updateOrder, example);

        return result == 1 ? true : false;
    }

    /**
     * 删除订单（逻辑删除）
     *
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteOrder(String userId, String orderId) {

        Orders deleteOrder = new Orders();
        deleteOrder.setIsDelete(YesOrNo.YES.type);
        deleteOrder.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);

        int result = ordersMapper.updateByExampleSelective(deleteOrder, example);
        return result == 1;
    }

    /**
     * 订单动向
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult getMyOrderTrend(String userId, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);

        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend(map);
        return setterPagedGrid(list,page);
    }


    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
