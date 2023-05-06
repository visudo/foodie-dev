# 用户中心

## 部署静态用户中心
* foodie-center

## 根据用户id查询用户信息
* com.demo.controller.center.CenterController.userInfo 查询用户信息

## 修改用户信息
* com.demo.controller.center.CenterUserController.update 更新用户信息
* 使用hibernate校验

## 上传用户图像
* 通过springboot来实现静态资源映射 com.demo.config.WebMvcConfig.addResourceHandlers
* com.demo.controller.center.CenterUserController.uploadFace 实现接口
* 限制文件大小 max-request-size，max-file-size 配置
* 捕获文件大小异常类型 CustomExceptionHandler

## 用户订单管理
* 查询订单 com.demo.controller.center.MyOrdersController.query
* service com.demo.service.impl.center.MyOrdersServiceImpl.queryMyOrders
* dao com.demo.mapper.OrdersMapperCustom.queryMyOrders
* 多表关联查询存在分页问题，使用子查询解决mapper/OrdersMapperCustom.xml:getSubItems

## 确认收获与删除订单
* com.demo.controller.center.MyOrdersController.confirmReceive
* com.demo.controller.center.MyOrdersController.delete

## 订单评价
* com.demo.controller.center.MyCommentsController
* 待评价内容展示
* 保存评价
* 评价数据展示

## 用户中心订单状态
* 订单概览 com.demo.controller.center.MyOrdersController.statusCounts
* 订单动向 com.demo.controller.center.MyOrdersController.trend