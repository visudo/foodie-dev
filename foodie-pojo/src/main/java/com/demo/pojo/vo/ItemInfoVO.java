package com.demo.pojo.vo;

import com.demo.pojo.Items;
import com.demo.pojo.ItemsImg;
import com.demo.pojo.ItemsParam;
import com.demo.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
