package com.demo.service.impl;


import com.demo.common.enums.CommentLevel;
import com.demo.common.enums.YesOrNo;
import com.demo.common.utils.DesensitizationUtil;
import com.demo.common.utils.PagedGridResult;
import com.demo.mapper.*;
import com.demo.pojo.*;
import com.demo.pojo.vo.CommentLevelCountsVO;
import com.demo.pojo.vo.ItemCommentVO;
import com.demo.pojo.vo.SearchItemVO;
import com.demo.pojo.vo.ShopcartVO;
import com.demo.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;
    /**
     * 根据商品id查询详情
     *
     * @param itemId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品id查询商品图片列表
     *
     * @param itemId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example itemsImgExp = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsImgExp.createCriteria();
        criteria.andEqualTo("itemId",itemId);

        return itemsImgMapper.selectByExample(itemsImgExp);
    }

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExp = new Example(ItemsSpec.class);
        Example.Criteria criteria = itemsSpecExp.createCriteria();
        criteria.andEqualTo("itemId",itemId);

        return itemsSpecMapper.selectByExample(itemsSpecExp);
    }



    /**
     * 根据商品id查询商品参数
     *
     * @param itemId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExp = new Example(ItemsParam.class);
        Example.Criteria criteria = itemsParamExp.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsParamMapper.selectOneByExample(itemsParamExp);
    }

    /**
     * 根据商品id查询评论数量
     *
     * @param itemId
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelCountsVO queryCommentCounts(String itemId) {

        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts+normalCounts+badCounts;

        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setBadCounts(badCounts);
        commentLevelCountsVO.setGoodCounts(goodCounts);
        commentLevelCountsVO.setNormalCounts(normalCounts);
        commentLevelCountsVO.setTotalCounts(totalCounts);
        return commentLevelCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);

        if (level!=null){
            condition.setCommentLevel(level);
        }

        return itemsCommentsMapper.selectCount(condition);
    }


    /**
     * 根据商品id查询商品评价（分页）
     *
     * @param itemId
     * @param level
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedComments(String itemId, Integer level,
                                              Integer page,Integer pageSize) {
        Map<String,Object> map =new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);

        PageHelper.startPage(page,pageSize);
        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);

        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }

        return setterPagedGrid(list,page);
    }

    /**
     * 根据信息搜索商品
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);

        PageHelper.startPage(page,pageSize);
        List<SearchItemVO> list = itemsMapperCustom.searchItems(map);

        return setterPagedGrid(list,page);
    }

    /**
     * 根据分类搜索商品
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);

        PageHelper.startPage(page,pageSize);
        List<SearchItemVO> list = itemsMapperCustom.searchItems(map);

        return setterPagedGrid(list,page);
    }

    /**
     * 根据规格ids查询最新的购物车中商品数据
     *
     * @param specIds
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] ids =specIds.split(",");
        ArrayList<Object> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList,ids);

        return  itemsMapperCustom.queryItemsBySpecIds(specIdsList);
    }

    /**
     * 根据规格id查询商品规格信息
     *
     * @param specId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemsSpecById(String specId) {
        return  itemsSpecMapper.selectByPrimaryKey(specId);
    }

    /**
     * 根据商品id获得商品图片主图
     *
     * @param itemId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);

        return result != null ? result.getUrl() : "";
    }

    /**
     * 减少库存
     *
     * @param specId
     * @param buyCounts
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public  void decreaseItemSpecStock(String specId, int buyCounts) {
        // 使用关键字synchronized不推荐，集群下无法锁住，性能低下
        // 锁数据库，导致数据库性能降低
        // 分布式锁 lockUtil.lock() 加锁

       /* //1、查询库存
        int stock =2;
        //2、判断库存是否能够减少到0以下
        if (stock - buyCounts <0 ){

        }*/

        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result!=1){
            throw new RuntimeException("订单创建失败，原因：库存不足");
        }
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
