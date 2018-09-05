package com.homvee.livebroadcast.dao.content;

import com.homvee.livebroadcast.dao.content.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:02
 */
public interface ContentDao extends JpaRepository<Content, Long>,ContentDaoExt {

    /**
     * find
     * @param roomId
     * @param userId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 and userId=?2 order by recentUsedTime limit 1" , nativeQuery = true)
    Content findByRoomIdAndUserId(Long roomId, Long userId);

    /**
     * find
     * @param roomId
     * @param userId
     * @param accountId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 and userId=?2 and acctId=?3 and used=0 order by id limit 1" , nativeQuery = true)
    Content findByRoomIdAndUserIdAndAndNotUsed(Long roomId, Long userId, Long accountId);
    /**
     * find
     * @param roomId
     * @param userId
     * @param accountId
     * @return
     */
    @Query(value = " select * from t_content where yn=1 and roomId=?1 and userId=?2 and acctId=?3 and used=1 order by id limit 1" , nativeQuery = true)
    Content findByRoomIdAndUserIdAndAndUsed(Long roomId, Long userId, Long accountId);

    /**
     * cnt
     * @param roomId
     * @return
     */
    @Query(value = " select count(id) from t_content where yn=1 and roomId=?1  and used=1" , nativeQuery = true)
    Integer countUsedByRoomId(Long roomId);

    /**
     * cnt
     * @param roomId
     * @return
     */
    @Query(value = " select count(id) from t_content where yn=1 and roomId=?1 " , nativeQuery = true)
    Integer countByRoomId(Long roomId);

    @Modifying
    @Query(value = " update t_content set used=0  where yn=1 and roomId=?1 " , nativeQuery = true)
    Integer resetUsed(Long roomId);
}
