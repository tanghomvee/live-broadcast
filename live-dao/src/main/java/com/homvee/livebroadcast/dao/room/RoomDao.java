package com.homvee.livebroadcast.dao.room;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Description Network Interface Card Dao
 * @date 2018-04-16 17:42
 */
public interface RoomDao extends JpaRepository<RoomDao, Long> , RoomDaoExt{


}
