package com.demo.service.center;


import com.demo.common.utils.PagedGridResult;
import com.demo.pojo.OrderItems;
import com.demo.pojo.bo.center.OrderItemsCommentBO;

import java.util.List;

public interface MyCommentsService {

    /**
     * 查询待评价的商品
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户评价
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);


    /**
     * 我的评价查询分页
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);

}
