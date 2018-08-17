package com.homvee.livebroadcast.dao.room.model;

import com.homvee.livebroadcast.dao.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:47
 */
@Entity
@Table(name = "t_room")
public class Room extends BaseEntity {

    private String roomName;

    private String url;

    private Long userId;

    /**
     * @see com.homvee.livebroadcast.common.enums.WayEnum
     * 房间聊天方式
     */
    private Integer way;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
