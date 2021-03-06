package com.homvee.livebroadcast.service.user;

import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.common.vos.RoomVO;
import com.homvee.livebroadcast.common.vos.UserVO;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.dao.user.model.User;
import com.homvee.livebroadcast.service.BaseService;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 10:26
 */
public interface UserService extends BaseService<User, Long> {

    /**
     *save
     * @param users
     * @return
     */
    List<User> save(List<User> users);


    /**
     * find by id
     * @param id
     * @return
     */
    User findOne(Long id);


    /**
     * find by condition
     * @param userVO
     * @param pager
     * @return
     */
    Pager findByConditions(UserVO userVO, Pager pager);

    /**
     * find by name & pwd
     * @param userName
     * @param pwd
     * @return
     */
    User findByUserNameAndPwd(String userName, String pwd);

    /**
     * find by auth-key
     * @param authKey
     * @return
     */
    User findByAuthKey(String authKey);
}
