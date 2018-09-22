package com.homvee.livebroadcast.service.sms.impl;

import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.dao.sms.PortInfoDao;
import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.sms.PortInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("portInfoService")
public class PortInfoServiceImpl extends BaseServiceImpl<PortInfo ,Long> implements PortInfoService {
    @Resource
    private PortInfoDao portInfoDao;

    @Override
    public PortInfo findByPhonNum(String phoneNum) {
        return portInfoDao.findByPhoNumAndYn(phoneNum , YNEnum.YES.getVal());
    }
}
