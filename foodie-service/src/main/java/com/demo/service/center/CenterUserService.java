package com.demo.service.center;


import com.demo.pojo.Users;
import com.demo.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 更新用户头像
     *
     * @param userId
     * @param faceUrl
     * @return
     */
    Users updateUserFace(String userId, String faceUrl);
}
