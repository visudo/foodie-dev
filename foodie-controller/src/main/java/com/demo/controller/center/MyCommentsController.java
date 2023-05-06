package com.demo.controller.center;


import com.demo.common.enums.YesOrNo;
import com.demo.common.utils.DemoJsonResult;
import com.demo.common.utils.PagedGridResult;
import com.demo.controller.BaseController;
import com.demo.pojo.OrderItems;
import com.demo.pojo.Orders;
import com.demo.pojo.bo.center.OrderItemsCommentBO;
import com.demo.service.center.MyCommentsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @PostMapping("/pending")
    public DemoJsonResult comments(
            @RequestParam String userId,
             @RequestParam String orderId){

        DemoJsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus()!= HttpStatus.OK.value()){
            return checkResult;
        }
        Orders myOrder = (Orders) checkResult.getData();
        if (myOrder.getIsComment() == YesOrNo.YES.type){
            return DemoJsonResult.errorMsg("该订单已经评价");
        }

        List<OrderItems> list = myCommentsService.queryPendingComment(orderId);
        return DemoJsonResult.ok(list);
    }



    @PostMapping("/saveList")
    public DemoJsonResult saveList(
            @RequestParam String userId,
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList){

        DemoJsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus()!= HttpStatus.OK.value()){
            return checkResult;
        }
        //判断评论内容List不能为空
        if (commentList==null || commentList.isEmpty() || commentList.size()==0){
            return DemoJsonResult.errorMsg("评论内容不能为空");
        }

        myCommentsService.saveComments(orderId,userId,commentList);

        return DemoJsonResult.ok();
    }

    @PostMapping("/query")
    public DemoJsonResult query(@RequestParam String userId,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize){

        if (StringUtils.isBlank(userId) ){
            return DemoJsonResult.errorMsg(null);
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myCommentsService.queryMyComments(userId, page, pageSize);

        return DemoJsonResult.ok(grid);
    }


}
