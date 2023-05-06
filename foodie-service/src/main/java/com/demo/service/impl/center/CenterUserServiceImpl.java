package com.demo.service.impl.center;

import com.demo.mapper.UsersMapper;
import com.demo.pojo.Users;
import com.demo.pojo.bo.center.CenterUserBO;
import com.demo.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;
    /**
     * 根据用户id查询用户信息
     *
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
        return user;
    }

    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO,updateUser);

        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);

        return queryUserInfo(userId);

    }

    /**
     * 更新用户头像
     *
     * @param userId
     * @param faceUrl
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
   }
