package com.homvee.livebroadcast.dao.sms;



public interface SendingSMSDaoExt  {

    /**
     * insert
     * @param mobile
     * @param content
     * @return
     */
    Long saveSendingSMS(String mobile, String content);
}
