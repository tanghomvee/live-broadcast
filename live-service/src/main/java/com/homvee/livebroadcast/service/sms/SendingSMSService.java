package com.homvee.livebroadcast.service.sms;

import com.homvee.livebroadcast.dao.sms.model.SendingSMS;

public interface SendingSMSService {
    /**
     * save
     * @param mobile
     * @param content
     * @return
     */
    Long saveSendingSMS(String mobile, String content);

    /**
     * save
     * @param sendingSMS
     * @return
     */
    SendingSMS save(SendingSMS sendingSMS);
}
