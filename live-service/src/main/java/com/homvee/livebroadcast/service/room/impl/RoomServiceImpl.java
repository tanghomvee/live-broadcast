package com.homvee.livebroadcast.service.room.impl;

import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.common.vos.RoomVO;
import com.homvee.livebroadcast.dao.room.RoomDao;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.room.RoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 10:27
 */
@Service("roomService")
public class RoomServiceImpl extends BaseServiceImpl<Room ,Long> implements RoomService {
    @Resource
    private RoomDao roomDao;

    @Override
    public List<Room> save(List<Room> rooms) {
        return roomDao.saveAll(rooms);
    }

    @Override
    public List<Room> findByUrlAndUserId(String url, Long userId) {
        return roomDao.findByUrlAndUserIdAndYn(url,userId , YNEnum.YES.getVal());
    }

    @Override
    public List<Room> findByRoomNameAndUserId(String roomName, Long userId) {
        return roomDao.findByRoomNameContainingAndUserIdAndYn(roomName,userId , YNEnum.YES.getVal());
    }

    @Override
    public Room findOne(Long id) {
        Optional<Room> optional = roomDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(RoomVO roomVO, Pager pager) {
        return roomDao.findByConditions(roomVO , pager);
    }

    @Override
    public Room findByUrl(String url) {
        return roomDao.findByUrlAndYn(url , YNEnum.YES.getVal());
    }
}
