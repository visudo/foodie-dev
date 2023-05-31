# 2

## 部署httpd图片服务器

* docker run -dit --name httpd -p 9999:80  httpd:latest
  httpd文件服务位置/var/www/html/
    
* 拷贝文件到httpd服务器
  docker cp -a /Users/xxxxxx/Downloads/chengzi.jpeg 9d97b77152c60b780bc01d6c6b51b4b40de853e3c148c3e6a96ebe8ff59bb4c3:/usr/local/apache2/htdocs/

## 生成轮播图
* CarouselService carousel接口实现
## 查询一级分类
* CategoryService index/cats 接口实现

## 查询二级分类
* 自定义mapper CategoryMapperCustom.java xml
* 自定义vo CategoryVO SubCategoryVO
* CategoryMapperCustom.xml语法生成集合数据
* 实现接口 index/subCat/{rootCatId}

## 查询首页一级分类下的数据
* 编写vo SimpleItemV，NewItemsVO
* 编写mapper
* 实现service controller层sixNewItems/{rootCatId}

## 商品详情页面
* 编写controller： /info/{itemId}
* 编写service：queryItemById，queryItemImgList，queryItemSpecList，queryItemParam
* 编写vo ItemInfoVO

## 商品好评数量展示
* 编写service com.demo.service.impl.ItemServiceImpl.queryCommentCounts
* 编写 vo  CommentLevelCountsVO
* 编写controller  /commentLevel

## 商品好评信息展示
* 编写 mapper ItemsMapperCustom.java ItemsMapperCustom.xml
* 编写 vo ItemCommentVO
* 编写service com.demo.service.impl.ItemServiceImpl.queryPagedComments
* 引入mybatis分页功能
　1.引入依赖
  2.配置分页helper
* 编写 controller /comments
* 实现敏感信息脱敏 DesensitizationUtil.commonDisplay

## 根据商品关键字搜索信息
* 编写 sql + mapper，searchItems
* 编写 vo，SearchItemVO
* 编写 service，com.demo.service.ItemService.searchItems
* 编写 controller，/search

## 根据商品id搜索信息
* 编写 sql + mapper，searchItemsByThirdCat
* 编写 service，com.demo.service.ItemService.searchItems
* 编写 controller，/catItems

## 购物车存储形式
* cookie
* session
* db
* redis

## 未登录情况下添加商品进购物车
* 如果cookie没购物车，创建购物车并添加商品
* 如果cookie存在同类商品，商品个数会增加

## 渲染购物车
* 编写 sql + mapper，queryItemsBySpecIds
* 编写 vo，ShopcartVO
* 编写 service，com.demo.controller.ItemsController.refresh
* 编写 controller，/refresh

## 购物车金额计算
* 前台代码

## 删除购物车中的商品

