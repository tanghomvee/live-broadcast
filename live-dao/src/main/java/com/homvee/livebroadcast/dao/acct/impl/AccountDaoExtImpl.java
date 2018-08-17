package com.homvee.livebroadcast.dao.acct.impl;

import com.google.common.collect.Maps;
import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.common.vos.AccountVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.JpaDaoSupport;
import com.homvee.livebroadcast.dao.acct.AccountDaoExt;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.room.model.Room;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:17
 */
public class AccountDaoExtImpl extends JpaDaoSupport<Account,Long>  implements AccountDaoExt {
    @Override
    public Pager findByConditions(AccountVO accountVO, Pager pager) {
        StringBuffer sql = new StringBuffer("SELECT * FROM t_room where yn=:yn ");

        Map<String , Object> params = Maps.newHashMap();
        params.put("yn" , YNEnum.YES.getVal());
        if (StringUtils.isEmpty(accountVO.getAcctName())){
            sql.append(" AND acctName like :acctName");
            params.put("acctName" , "%" + accountVO.getAcctName() + "%");
        }
        if (StringUtils.isEmpty(accountVO.getUserId())){
            sql.append(" AND userId = :userId");
            params.put("userId" ,accountVO.getUserId() );
        }
        sql.append("  order by   createTime desc");

        try{

            Pager retPager = super.doSQLPage(sql.toString() , params , Room.class , pager.getPageNum() ,pager.getPageSize());
            return retPager;
        }catch (Exception ex){
            LOGGER.error("分页查询账号数据异常,sql={} ,params={}" ,sql , params ,ex);
        }
        return null;
    }
}
