package com.homvee.livebroadcast.service.room;

import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.common.vos.RoomVO;
import com.homvee.livebroadcast.dao.room.model.Room;
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
public interface RoomService extends BaseService<Room, Long> {

    /**
     *save
     * @param rooms
     * @return
     */
    List<Room> save(List<Room> rooms);

    /**
     * find by url & userId
     * @param url
     * @param userId
     * @return
     */
    List<Room> findByUrlAndUserId(String url, Long userId);

    /**
     * find by name
     * @param roomName
     * @param userId
     * @return
     */
    List<Room> findByRoomNameAndUserId(String roomName, Long userId);

    /**
     * find by id
     * @param id
     * @return
     */
    Room findOne(Long id);


    /**
     * find by condition
     * @param roomVO
     * @param pager
     * @return
     */
    Pager findByConditions(RoomVO roomVO, Pager pager);

    /**
     * find by url
     * @param url
     * @return
     */
    Room findByUrl(String url);

    /**
     * find by userid
     * @param userId
     * @return
     */
    List<Room> findByUserId(Long userId);

    /**
     * del
     * @param ids
     * @return
     */
    Integer delByIds(Long[] ids);

    /**
     * find all
     * @return
     */
    List<Room> findAll();
}
