package com.homvee.livebroadcast.dao.sms;


import com.homvee.livebroadcast.dao.sms.model.SendingSMS;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SendingSMSDao extends JpaRepository<SendingSMS, Long> , SendingSMSDaoExt {

}
