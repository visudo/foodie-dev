package com.demo.service;

import com.demo.common.utils.PagedGridResult;
import com.demo.pojo.Items;
import com.demo.pojo.ItemsImg;
import com.demo.pojo.ItemsParam;
import com.demo.pojo.ItemsSpec;
import com.demo.pojo.vo.CommentLevelCountsVO;
import com.demo.pojo.vo.ShopcartVO;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品id查询详情
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询评论数量
     * @param itemId
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品评价（分页）
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level,
                                              Integer page, Integer pageSize);
    /**
     * 根据分类搜索商品
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);


    /**
     * 根据分类搜索商品
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车中商品数据
     *
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据规格id查询商品规格信息
     *
     * @param specId
     * @return
     */
    public ItemsSpec queryItemsSpecById(String specId);


    /**
     * 根据商品id获得商品图片主图
     *
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     *
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemSpecStock(String specId, int buyCounts);
}
