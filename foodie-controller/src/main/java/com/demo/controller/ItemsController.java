package com.demo.controller;


import com.demo.common.utils.DemoJsonResult;
import com.demo.common.utils.PagedGridResult;
import com.demo.pojo.Items;
import com.demo.pojo.ItemsImg;
import com.demo.pojo.ItemsParam;
import com.demo.pojo.ItemsSpec;
import com.demo.pojo.vo.CommentLevelCountsVO;
import com.demo.pojo.vo.ItemInfoVO;
import com.demo.pojo.vo.ShopcartVO;
import com.demo.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemsController extends BaseController{

    @Autowired
    private ItemService itemService;

     @GetMapping("/info/{itemId}")
    public DemoJsonResult info(@PathVariable String itemId){

        if (StringUtils.isBlank(itemId)){
            return DemoJsonResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        ItemsParam itemParams = itemService.queryItemParam(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemSpecList);
        itemInfoVO.setItemParams(itemParams);

        return DemoJsonResult.ok(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    public DemoJsonResult commentLevel(@RequestParam String itemId){
        if (StringUtils.isBlank(itemId)){
            return DemoJsonResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return DemoJsonResult.ok(countsVO);
    }

    @GetMapping("/comments")
    public DemoJsonResult comments(@RequestParam String itemId,
                                    @RequestParam(required = false) Integer level,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize){

        if (StringUtils.isBlank(itemId)){
            return DemoJsonResult.errorMsg(null);
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);
        return DemoJsonResult.ok(grid);
    }

   @GetMapping("/search")
    public DemoJsonResult search( @RequestParam String keywords,
                                  @RequestParam String sort,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize){

        if (StringUtils.isBlank(keywords)){
            return DemoJsonResult.errorMsg(null);
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItems(keywords, sort, page, pageSize);
        return DemoJsonResult.ok(grid);
    }

    @GetMapping("/catItems")
    public DemoJsonResult catItems( @RequestParam Integer catId,
                                    @RequestParam String sort,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize){

        if (catId == null){
            return DemoJsonResult.errorMsg(null);
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItems(catId, sort, page, pageSize);
        return DemoJsonResult.ok(grid);
    }

    @GetMapping("/refresh")
    public DemoJsonResult refresh(@RequestParam String itemSpecIds){
        if (StringUtils.isBlank(itemSpecIds)){
            return DemoJsonResult.ok();
        }
        List<ShopcartVO> shopcart = itemService.queryItemsBySpecIds(itemSpecIds);
        return DemoJsonResult.ok(shopcart);
    }
}
