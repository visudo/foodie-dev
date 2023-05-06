package com.demo.service.impl.center;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demo.common.enums.YesOrNo;
import com.demo.common.utils.PagedGridResult;
import com.demo.mapper.ItemsCommentsMapperCustom;
import com.demo.mapper.OrderItemsMapper;
import com.demo.mapper.OrderStatusMapper;
import com.demo.mapper.OrdersMapper;
import com.demo.pojo.OrderItems;
import com.demo.pojo.OrderStatus;
import com.demo.pojo.Orders;
import com.demo.pojo.bo.center.OrderItemsCommentBO;
import com.demo.pojo.vo.MyCommentVO;
import com.demo.service.BaseService;
import com.demo.service.center.MyCommentsService;
import com.github.pagehelper.PageHelper;

@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private ItemsCommentsMapperCustom orderItemsMapperCustom;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private Sid sid;
    /**
     * 查询待评价的商品
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {

        OrderItems query = new OrderItems();
        query.setOrderId(orderId);

        return orderItemsMapper.select(query);
    }


    /**
     * 保存用户评价
     *
     * @param orderId
     * @param userId
     * @param commentList
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        //1、保存评价 item_comments
        for (OrderItemsCommentBO oic : commentList) {
            oic.setCommentId(sid.nextShort());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("commentList",commentList);
        orderItemsMapperCustom.saveComments(map);

        //2、修改订单表为已评价   orders
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(order);


        //3、订单状态表留言时间   order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    /**
     * 我的评价查询分页
     *
     * @param userId
     * @param page
     * @param pageSize
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);

        PageHelper.startPage(page,pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);

        return setterPagedGrid(list,page);
    }
}
