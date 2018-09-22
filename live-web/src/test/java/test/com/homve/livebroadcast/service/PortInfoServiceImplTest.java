package test.com.homve.livebroadcast.service;

import com.homvee.livebroadcast.dao.content.model.Content;
import com.homvee.livebroadcast.dao.sms.PortInfoDao;
import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import com.homvee.livebroadcast.service.content.ContentService;
import com.homvee.livebroadcast.service.sms.PortInfoService;
import org.junit.Test;
import test.com.homve.BaseTest;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-06 14:18
 */
public class PortInfoServiceImplTest extends BaseTest {

    @Resource
    private PortInfoDao portInfoDao
            ;
    @Test
    public void save() {
        PortInfo portInfo = new PortInfo();
        portInfo.setPhoNum("18108691373");
        portInfo.setPortNum(2);
        portInfo.setImsi("imsi");
        portInfo.setIccid("iccid");
        portInfo = portInfoDao.save(portInfo);
        System.out.printf("");
    }

}