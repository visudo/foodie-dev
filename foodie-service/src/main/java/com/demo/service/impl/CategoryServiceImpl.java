package com.demo.service.impl;

import com.demo.mapper.CategoryMapper;
import com.demo.mapper.CategoryMapperCustom;
import com.demo.pojo.Category;
import com.demo.pojo.vo.CategoryVO;
import com.demo.pojo.vo.NewItemsVO;
import com.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;
    /**
     * 查询所有一级分类
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queAllRootLevelCat() {
        Example example = new Example(Category.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);

        List<Category> result = categoryMapper.selectByExample(example);
        return result;
    }

    /**
     * 根据一级分类id查询子分类信息
     *
     * @param rootCatId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }


    /**
     * 查询首页一级分类下的数据
     *
     * @param rootCatId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rootCatId",rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}
