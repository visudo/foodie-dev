package com.demo.mapper;

import com.demo.pojo.OrderStatus;
import com.demo.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String,Object> map);

    int getMyOrderStatusCounts(@Param("paramsMap")Map<String,Object> map);

    List<OrderStatus> getMyOrderTrend(@Param("paramsMap")Map<String,Object> map);
}
