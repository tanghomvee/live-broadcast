package com.homvee.livebroadcast.dao.sms;


import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PortInfoDao extends JpaRepository<PortInfo, Long>  {

    PortInfo findByPhoNumAndYn(String phoneNum, Integer yn);

    @Query(value = "select * from port_info where yn=1 order by rand()  limit 1" , nativeQuery = true)
    PortInfo findRandPhoneNum();
}
