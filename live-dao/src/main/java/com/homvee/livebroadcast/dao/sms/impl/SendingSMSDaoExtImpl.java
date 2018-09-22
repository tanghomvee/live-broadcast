package com.homvee.livebroadcast.dao.sms.impl;

import com.homvee.livebroadcast.dao.JpaDaoSupport;
import com.homvee.livebroadcast.dao.sms.SendingSMSDaoExt;
import com.homvee.livebroadcast.dao.sms.model.SendingSMS;

public class SendingSMSDaoExtImpl extends JpaDaoSupport<SendingSMS,Long> implements SendingSMSDaoExt {
    @Override
    public Long saveSendingSMS(String mobile, String content) {
        return null;
    }
}
