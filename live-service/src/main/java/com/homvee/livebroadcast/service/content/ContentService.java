package com.homvee.livebroadcast.service.content;

import com.homvee.livebroadcast.common.vos.ContentVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.content.model.Content;
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
public interface ContentService extends BaseService<Content, Long> {

    /**
     *
     * @param contents
     * @return
     */
    List<Content> save(List<Content> contents);


    /**
     * find by id
     * @param id
     * @return
     */
    Content findOne(Long id);


    /**
     * find page
     * @param contentVO
     * @param pager
     * @return
     */
    Pager findByConditions(ContentVO contentVO, Pager pager);

    /**
     * find recently content
     * @param roomId
     * @param userId
     * @return
     */
    Content autoContent(Long roomId , Long userId);

    /**
     * find
     * @param id
     * @param userId
     * @param accountId
     * @return
     */
    Content nextContent(Long id, Long userId, Long accountId);

    /**
     * del
     * @param ids
     */
    void delByIds(Long[] ids);

    /**
     * 循环去对话
     * @param roomId
     * @param userId
     * @param acctId
     * @return
     */
    Content loopContent(Long roomId, Long userId, Long acctId);
}
