package com.homvee.livebroadcast.dao.room.impl;

import com.google.common.collect.Maps;
import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.common.vos.RoomVO;
import com.homvee.livebroadcast.dao.JpaDaoSupport;
import com.homvee.livebroadcast.dao.room.RoomDaoExt;
import com.homvee.livebroadcast.dao.room.model.Room;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:50
 */
public class RoomDaoExtImpl extends JpaDaoSupport<Room,Long> implements RoomDaoExt {
    @Override
    public Pager findByConditions(RoomVO roomVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT * FROM t_room where yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (!StringUtils.isEmpty(roomVO.getRoomName())){
            sql.append(" AND roomName like :roomName");
            params.put("roomName" , "%" + roomVO.getRoomName() + "%");
        }
        if (!StringUtils.isEmpty(roomVO.getUrl())){
            sql.append(" AND url like :url");
            params.put("url" ,"%" + roomVO.getUrl() + "%");
        }
        if (!StringUtils.isEmpty(roomVO.getUserId())){
            sql.append(" AND userId = :userId");
            params.put("userId" ,roomVO.getUserId() );
        }
        if (!StringUtils.isEmpty(roomVO.getWay())){
            sql.append(" AND way = :way");
            params.put("way" ,roomVO.getWay() );
        }
        sql.append(" order by   createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , Room.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询直播间数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
