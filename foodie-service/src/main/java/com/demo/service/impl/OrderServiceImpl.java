package com.demo.service.impl;


import com.demo.common.enums.OrderStatusEnum;
import com.demo.common.enums.YesOrNo;
import com.demo.common.utils.DateUtil;
import com.demo.mapper.OrderItemsMapper;
import com.demo.mapper.OrderStatusMapper;
import com.demo.mapper.OrdersMapper;
import com.demo.pojo.*;
import com.demo.pojo.bo.SubmitOrderBO;
import com.demo.pojo.vo.MerchantOrdersVO;
import com.demo.pojo.vo.OrderVO;
import com.demo.service.AddressService;
import com.demo.service.ItemService;
import com.demo.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    /**
     * 创建一个订单
     *
     * @param submitOrderBO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        //包邮。邮费为0
        Integer postAmount = 0;

        String orderId = sid.nextShort();
        UserAddress address = addressService.queryUserAddress(userId, addressId);
        //1、新订单数据保存
        Orders newOrders = new Orders();
        newOrders.setId(orderId);
        newOrders.setUserId(userId);

        newOrders.setReceiverName(address.getReceiver());
        newOrders.setReceiverMobile(address.getMobile());
        newOrders.setReceiverAddress(address.getProvince() + " "
                + address.getCity() + " "
                + address.getDistrict() + " "
                + address.getDetail());


        newOrders.setPostAmount(postAmount);
        newOrders.setPayMethod(payMethod);
        newOrders.setLeftMsg(leftMsg);
        newOrders.setIsComment(YesOrNo.NO.type);
        newOrders.setIsDelete(YesOrNo.NO.type);
        newOrders.setCreatedTime(new Date());
        newOrders.setUpdatedTime(new Date());

        //2、根据itemSpecIds保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String itemSpecId : itemSpecIdArr) {

            //TODO 整合redis后，商品购买的数量从redis中获取
            int buyCount = 1;
            //2.1 根据规格id查询规格具体信息，主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemsSpecById(itemSpecId);
            totalAmount +=itemsSpec.getPriceNormal() * buyCount;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCount;

            //2.2 根据商品id获取商品信息以及图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            //2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCount);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());

            orderItemsMapper.insert(subOrderItem);

            //2.4 在用户提交订单后，商品规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId,buyCount);

        }
        newOrders.setTotalAmount(totalAmount);
        newOrders.setRealPayAmount(realPayAmount);

        ordersMapper.insert(newOrders);

        //3、保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);


        //4、构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount+postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        //5、构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);


        return orderVO;
    }

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 关闭超时未支付订单
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {
        // 查询所有未支付订单，判断时间是否超过一天
        OrderStatus queryOrder = new OrderStatus();
        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrder);

        for (OrderStatus os : list) {
            // 获得订单床架时间
            Date createdTime = os.getCreatedTime();
            //和当前时间进行对比
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days>=1){
                //超过一天关闭订单
                doCloseOrder(os.getOrderId());
            }
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId){
        OrderStatus close = new OrderStatus();
        close.setOrderId(orderId);
        close.setOrderStatus(OrderStatusEnum.CLOSE.type);
        close.setCloseTime(new Date());

        orderStatusMapper.selectByPrimaryKey(close);
    }
}
