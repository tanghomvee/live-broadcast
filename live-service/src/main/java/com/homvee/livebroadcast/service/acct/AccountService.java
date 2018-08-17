package com.homvee.livebroadcast.service.acct;

import com.homvee.livebroadcast.common.vos.AccountVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.acct.model.Account;
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
public interface AccountService extends BaseService<Account, Long> {

    /**
     *
     * @param accts
     * @return
     */
    List<Account> save(List<Account> accts);


    /**
     * find by name
     * @param acctName
     * @param userId
     * @return
     */
    List<Account> findByAcctNameAndUserId(String acctName, Long userId);

    /**
     * find by id
     * @param id
     * @return
     */
    Account findOne(Long id);


    /**
     * find page
     * @param accountVO
     * @param pager
     * @return
     */
    Pager findByConditions(AccountVO accountVO, Pager pager);
}
