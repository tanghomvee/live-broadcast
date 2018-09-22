package com.homvee.livebroadcast.service.sms;

import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import com.homvee.livebroadcast.service.BaseService;

public interface PortInfoService  extends BaseService<PortInfo, Long> {

    PortInfo findByPhonNum(String phoneNum);
}

