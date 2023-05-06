package com.demo.controller;

import com.demo.common.utils.DemoJsonResult;
import com.demo.common.utils.MobileEmailUtils;
import com.demo.pojo.UserAddress;
import com.demo.pojo.bo.AddressBO;
import com.demo.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    /**
     * 在用户确认订单页面，可以针对收货地址做出如下操作
     * 1、查询用户所有收货地址列表
     * 2、新增收货地址
     * 3、删除收货地址
     * 4、编辑收货地址
     * 5、设置默认地址
     */

     @PostMapping("/list")
    public DemoJsonResult list(@RequestParam String userId){

        if (StringUtils.isBlank(userId)){
            return DemoJsonResult.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId);
        return DemoJsonResult.ok(list);
    }


     @PostMapping("/add")
    public DemoJsonResult add(@RequestBody AddressBO addressBO){
        DemoJsonResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() !=200){
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);
        return DemoJsonResult.ok();
    }

    @PostMapping("/update")
    public DemoJsonResult update(@RequestBody AddressBO addressBO){
        if (StringUtils.isBlank(addressBO.getAddressId())){
            return DemoJsonResult.errorMsg("修改地址错误: addressId不能为空");
        }
        DemoJsonResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() !=200){
            return checkRes;
        }

        addressService.updateUserAddress(addressBO);
        return DemoJsonResult.ok();
    }

    @PostMapping("/delete")
    public DemoJsonResult delete(@RequestParam String userId,
                                  @RequestParam String addressId){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return DemoJsonResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId,addressId);
        return DemoJsonResult.ok();
    }


    @PostMapping("/setDefalut")
    public DemoJsonResult setDefalut(@RequestParam String userId,
                                  @RequestParam String addressId){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return DemoJsonResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId,addressId);
        return DemoJsonResult.ok();
    }



    private DemoJsonResult checkAddress(AddressBO addressBO){
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)){
            return DemoJsonResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12){
            return DemoJsonResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)){
            return DemoJsonResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length()!=11){
            return DemoJsonResult.errorMsg("收货人手机号长度不正确");
        }

        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk){
            return DemoJsonResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province)||
                StringUtils.isBlank(city)||
                StringUtils.isBlank(district)||
                StringUtils.isBlank(detail)){
            return DemoJsonResult.errorMsg("收货信息不能为空");
        }

        return DemoJsonResult.ok();
    }
}
