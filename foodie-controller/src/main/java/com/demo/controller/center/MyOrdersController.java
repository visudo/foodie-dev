package com.demo.controller.center;


import com.demo.common.utils.DemoJsonResult;
import com.demo.common.utils.PagedGridResult;
import com.demo.controller.BaseController;
import com.demo.pojo.vo.OrderStatusCountsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myorders")
public class MyOrdersController extends BaseController {

//    @Autowired
//    private MyOrdersService myOrdersService;


     @PostMapping("/query")
    public DemoJsonResult query(@RequestParam String userId,
                                @RequestParam(required = false) Integer orderStatus,
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

        PagedGridResult grid = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return DemoJsonResult.ok(grid);
    }

    //商家没有后端，此接口仅用于模拟
     @GetMapping("/deliver")
    public DemoJsonResult deliver(
            @RequestParam String orderId){
        if (StringUtils.isBlank(orderId)){
            return DemoJsonResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);

        return DemoJsonResult.ok();
    }


    @PostMapping("/confirmReceive")
    public DemoJsonResult confirmReceive(
            @RequestParam String orderId,
            @RequestParam String userId){

        DemoJsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus()!= HttpStatus.OK.value()){
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res){
            return DemoJsonResult.errorMsg("订单确认收货失败");
        }

        return DemoJsonResult.ok();
    }

    @PostMapping("/delete")
    public DemoJsonResult delete(
            @RequestParam String orderId,
            @RequestParam String userId){

        DemoJsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus()!= HttpStatus.OK.value()){
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res){
            return DemoJsonResult.errorMsg("订单删除失败");
        }
        return DemoJsonResult.ok();
    }

    @PostMapping("/statusCounts")
    public DemoJsonResult statusCounts(
            @RequestParam String userId){

        if (StringUtils.isBlank(userId)){
            return DemoJsonResult.errorMsg(null);
        }

        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);
        return DemoJsonResult.ok(result);

    }

    @PostMapping("/trend")
    public DemoJsonResult trend(
            @RequestParam String userId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId) ){
            return DemoJsonResult.errorMsg(null);
        }

        if (page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myOrdersService.getMyOrderTrend(userId, page, pageSize);
        return DemoJsonResult.ok(grid);
    }


}
