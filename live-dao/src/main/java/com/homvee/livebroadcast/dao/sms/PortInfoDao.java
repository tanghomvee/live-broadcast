package com.homvee.livebroadcast.dao.sms;


import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import com.homvee.livebroadcast.dao.sms.model.SendingSMS;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PortInfoDao extends JpaRepository<PortInfo, Long>  {

    PortInfo findByPhoNumAndYn(String phoneNum, Integer yn);
}
