package com.demo.service;


import com.demo.pojo.Category;
import com.demo.pojo.vo.CategoryVO;
import com.demo.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queAllRootLevelCat();

    /**
     * 根据一级分类id查询子分类信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页一级分类下的数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
