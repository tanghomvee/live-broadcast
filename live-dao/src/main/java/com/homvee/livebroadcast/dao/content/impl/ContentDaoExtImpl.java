package com.homvee.livebroadcast.dao.content.impl;

import com.google.common.collect.Maps;
import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.common.vos.ContentVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.JpaDaoSupport;
import com.homvee.livebroadcast.dao.content.ContentDaoExt;
import com.homvee.livebroadcast.dao.content.model.Content;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:36
 */
public class ContentDaoExtImpl extends JpaDaoSupport<Content,Long> implements ContentDaoExt {
    @Override
    public Pager findByConditions(ContentVO contentVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT * FROM t_content where yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (StringUtils.isEmpty(contentVO.getContent())){
            sql.append(" AND content like :content");
            params.put("content" , "%" + contentVO.getContent() + "%");
        }

        if (StringUtils.isEmpty(contentVO.getUserId())){
            sql.append(" AND userId = :userId");
            params.put("userId" ,contentVO.getUserId() );
        }
        if (StringUtils.isEmpty(contentVO.getAcctId())){
            sql.append(" AND acctId = :acctId");
            params.put("acctId" ,contentVO.getAcctId() );
        }
        if (StringUtils.isEmpty(contentVO.getCatgId())){
            sql.append(" AND catgId = :catgId");
            params.put("catgId" ,contentVO.getCatgId() );
        }
        if (StringUtils.isEmpty(contentVO.getRoomId())){
            sql.append(" AND roomId = :roomId");
            params.put("roomId" ,contentVO.getRoomId() );
        }



        sql.append(" order by useNum ,createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , Content.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询直内容数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
