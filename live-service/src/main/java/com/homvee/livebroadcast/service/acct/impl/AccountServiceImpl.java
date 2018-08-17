package com.homvee.livebroadcast.service.acct.impl;

import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.common.vos.AccountVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.acct.AccountDao;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.acct.AccountService;
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
 * @date 2018-08-17 13:45
 */
@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl<Account,Long> implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public List<Account> save(List<Account> accts) {
        return accountDao.saveAll(accts);
    }

    @Override
    public List<Account> findByAcctNameAndUserId(String acctName, Long userId) {
        return accountDao.findByAcctNameAndUserIdAndYn(acctName, userId, YNEnum.YES.getVal());
    }

    @Override
    public Account findOne(Long id) {
        Optional<Account> optional = accountDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(AccountVO accountVO, Pager pager) {
        return accountDao.findByConditions(accountVO , pager);
    }
}
