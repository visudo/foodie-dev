# 3

## 地址列表相关
* bo编写 AddressBO
* service编写 AddressService
* controller编写 AddressController

## 创建订单
* 根据商品规格id获取规格信息 com.demo.service.impl.ItemServiceImpl.queryItemsSpecById
* 根据用户地址id获取用户地址信息 com.demo.service.AddressService.queryUserAddress

## 使用微信&支付宝支付

## 使用定时任务来关闭订单
* com.demo.config.OrderJob.autoCloseOrder 实现
* 定时任务存在弊端  
1、会有时间差，程序不严谨  
2、不支持集群  
3、会对数据库全表搜索
4、定时任务只适用小型项目，后续需要使用消息队列消费，延时队列时间差小
