package com.homvee.livebroadcast.dao.acct;

import com.homvee.livebroadcast.dao.acct.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:07
 */
public interface AccountDao extends JpaRepository<Account, Long> , AccountDaoExt {
    /**
     * find by like name
     * @param acctName
     * @param userId
     * @param yn
     * @return
     */
    List<Account> findByAcctNameAndUserIdAndYn(String acctName, Long userId, Integer yn);

}