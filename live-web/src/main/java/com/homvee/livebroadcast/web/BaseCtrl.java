package com.homvee.livebroadcast.web;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.homvee.livebroadcast.common.constants.SessionKey;
import com.homvee.livebroadcast.common.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 09:34
 */
public class BaseCtrl {
    protected Logger LOGGER = null;

    @Resource
    protected HttpSession session;
    protected LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
            //最多存放1000个数据
            .maximumSize(1000)
            //缓存1H，1H之后进行回收
            .expireAfterWrite(8, TimeUnit.HOURS)
            //开启，记录状态数据功能
            .recordStats()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(10)
            .build(new CacheLoader<String, Long>() {
                //数据加载，默认返回-1，也可以是查询操作，如从DB查询
                @Override
                public Long load(String key) throws Exception {
                    // TODO Auto-generated method stub
                    return -1L;
                }
            });


    public BaseCtrl() {
        LOGGER = LoggerFactory.getLogger(this.getClass());
    }


    protected UserVO getUser() {

        return (UserVO) session.getAttribute(SessionKey.USER);

    }

    protected void setUser(UserVO user) {
      session.setAttribute(SessionKey.USER , user);
    }
    protected void setRoomChatInterval(Long roomId ,Long time) {
        cache.put(roomId.toString() , time);
    }
    protected Long getRoomChatInterval(Long roomId) {
        try {
            return cache.get(roomId.toString());
        } catch (ExecutionException e) {
            LOGGER.error("" , e);
        }

        return -1L;
    }
}
