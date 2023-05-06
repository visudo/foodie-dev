package com.demo.service;

import com.demo.pojo.UserAddress;
import com.demo.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id查询收货地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 增加收货地址
     * @param addressBO
     */
    public void addNewUserAddress(AddressBO addressBO);


    /**
     * 修改收货地址
     * @param addressBO
     */
    public void updateUserAddress(AddressBO addressBO);


    /**
     * 删除收货地址
     * @param userId
     * @param addressId
     */
    public void deleteUserAddress(String userId,String addressId);

    /**
     * 修改地址为默认地址
     * @param userId
     * @param addressId
     */
    public void updateUserAddressToBeDefault(String userId,String addressId);

    /**
     * 根据用户id和地址id，查询具体的用户地址对象信息
     * @param userId
     * @param addressId
     * @return
     */
    public UserAddress queryUserAddress(String userId,String addressId);
}
