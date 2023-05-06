package com.demo.mapper;


import com.demo.pojo.vo.ItemCommentVO;
import com.demo.pojo.vo.SearchItemVO;
import com.demo.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String,Object> map);

    List<SearchItemVO> searchItems(@Param("paramsMap") Map<String,Object> map);

    List<SearchItemVO> searchItemsByThirdCat(@Param("paramsMap") Map<String,Object> map);

    List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIds);

    int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);


}
