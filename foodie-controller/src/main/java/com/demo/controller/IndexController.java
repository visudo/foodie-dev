package com.demo.controller;

import com.demo.common.enums.YesOrNo;
import com.demo.common.utils.DemoJsonResult;
import com.demo.pojo.Carousel;
import com.demo.pojo.Category;
import com.demo.pojo.vo.CategoryVO;
import com.demo.pojo.vo.NewItemsVO;
import com.demo.service.CarouselService;
import com.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/carousel")
    public DemoJsonResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);
        return DemoJsonResult.ok(list);
    }


    /**
     * 首页分类展示
     * 1、第一次刷新主页查询大分类，并渲染到主页
     * 2、鼠标移动到大分类，再加载子分类
     *
     */
    @GetMapping("/cats")
    public DemoJsonResult cats(){
        List<Category> list = categoryService.queAllRootLevelCat();
        return DemoJsonResult.ok(list);
    }

    @GetMapping("/subCat/{rootCatId}")
    public DemoJsonResult subCat(@PathVariable Integer rootCatId){
        if (rootCatId ==null){
            return DemoJsonResult.errorMsg("分类不存在");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return DemoJsonResult.ok(subCatList);
    }

    @GetMapping("/sixNewItems/{rootCatId}")
    public DemoJsonResult sixNewItems(@PathVariable Integer rootCatId){
        if (rootCatId ==null){
            return DemoJsonResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> newItemsList = categoryService.getSixNewItemsLazy(rootCatId);
        return DemoJsonResult.ok(newItemsList);
    }
}