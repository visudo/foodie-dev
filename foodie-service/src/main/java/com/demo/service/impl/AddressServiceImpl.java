package com.demo.service.impl;


import com.demo.common.enums.YesOrNo;
import com.demo.mapper.UserAddressMapper;
import com.demo.pojo.UserAddress;
import com.demo.pojo.bo.AddressBO;
import com.demo.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    /**
     * 根据用户id查询收货地址列表
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        return userAddressMapper.select(userAddress);
    }

    /**
     * 增加收货地址
     *
     * @param addressBO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewUserAddress(AddressBO addressBO) {
        //1、判断当前用户是否存在地址，如果没有，则新增为默认地址
        Integer isDefault = 0;
        List<UserAddress> addressList = this.queryAll(addressBO.getUserId());
        if (addressList == null || addressList.isEmpty() || addressList.size() == 0){
            isDefault = 1;
        }

        String addressId = sid.nextShort();
        //2、保存地址到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,newAddress);

        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(newAddress);
    }

    /**
     * 修改收货地址
     *
     * @param addressBO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();

        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,pendingAddress);

        pendingAddress.setId(addressId);
        pendingAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(pendingAddress);
    }

    /**
     * 删除收货地址
     *
     * @param userId
     * @param addressId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setId(addressId);
        address.setUserId(userId);

        userAddressMapper.delete(address);
    }

    /**
     * 修改地址为默认地址
     *
     * @param userId
     * @param addressId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        //1、查找默认地址，设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> list = userAddressMapper.select(queryAddress);
        for (UserAddress address : list) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }

        //2、根据地址id修改为默认地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);


    }

    /**
     * 根据用户id和地址id，查询具体的用户地址对象信息
     *
     * @param userId
     * @param addressId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        return userAddressMapper.selectOne(userAddress);
    }
}
