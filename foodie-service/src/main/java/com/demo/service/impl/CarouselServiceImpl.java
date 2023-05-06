package com.demo.service.impl;

import com.demo.mapper.CarouselMapper;
import com.demo.pojo.Carousel;
import com.demo.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;
    /**
     * 查询所有轮播图
     *
     * @param isShow
     * @return
     */
    @Override
    public List<Carousel> queryAll(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow",isShow);
        List<Carousel> carouselList = carouselMapper.selectByExample(example);


        return carouselList;
    }
}
