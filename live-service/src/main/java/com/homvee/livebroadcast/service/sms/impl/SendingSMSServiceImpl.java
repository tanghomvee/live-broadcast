package com.homvee.livebroadcast.service.sms.impl;

import com.homvee.livebroadcast.dao.sms.SendingSMSDao;
import com.homvee.livebroadcast.dao.sms.model.SendingSMS;
import com.homvee.livebroadcast.service.sms.SendingSMSService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sendingSMSService")
public class SendingSMSServiceImpl implements SendingSMSService {

    @Resource
    private SendingSMSDao sendingSMSDao;

    @Override
    public Long saveSendingSMS(String mobile, String content) {
        return sendingSMSDao.saveSendingSMS(mobile ,content);
    }

    @Override
    public SendingSMS save(SendingSMS sendingSMS) {
        return sendingSMSDao.save(sendingSMS);
    }
}
