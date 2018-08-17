package com.homvee.livebroadcast.dao.room;

import com.homvee.livebroadcast.dao.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface RoomDao extends JpaRepository<Room, Long> , RoomDaoExt{


    /**
     * find by url
     * @param url
     * @param userId
     * @param yn
     * @return
     */
    List<Room> findByUrlAndUserIdAndYn(String url, Long userId, Integer yn);

    /**
     * find by like roomName
     * @param roomName
     * @param userId
     * @param yn
     * @return
     */
    List<Room> findByRoomNameContainingAndUserIdAndYn(String roomName, Long userId, Integer yn);

    /**
     * find by url
     * @param url
     * @param val
     * @return
     */
    Room findByUrlAndYn(String url, Integer val);
}
