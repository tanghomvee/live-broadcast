package com.homvee.livebroadcast.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.homvee.livebroadcast.common.vos.Msg;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import com.homvee.livebroadcast.dao.sms.model.SendingSMS;
import com.homvee.livebroadcast.dao.user.model.User;
import com.homvee.livebroadcast.service.acct.AccountService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.sms.PortInfoService;
import com.homvee.livebroadcast.service.sms.SendingSMSService;
import com.homvee.livebroadcast.service.user.UserService;
import com.homvee.livebroadcast.web.BaseCtrl;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path = "/sms")
public class SMSCtrl extends BaseCtrl {

    @Resource
    private RoomService roomService;
    @Resource
    private AccountService accountService;
    @Resource
    private SendingSMSService sendingSMSService;

    @Resource
    private PortInfoService portInfoService;

    @Resource
    private UserService userService;

    @RequestMapping(path = {"/send"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg sendMsg(String acctName, String authKey , String toPhone , String smsContent){
        if(StringUtils.isEmpty(acctName) || StringUtils.isEmpty(authKey) || StringUtils.isEmpty(toPhone)){
            return Msg.error("参数错误");
        }

        User user = userService.findByAuthKey(authKey);
        if(user == null){
            return Msg.error("非法账户");
        }
        List<Account> accts = accountService.findByAcctNameAndUserId(acctName.trim() , user.getId());
        if (CollectionUtils.isEmpty(accts)){
            return Msg.error("账号不存在");
        }
        JSONObject retJSON = new JSONObject();
        retJSON.put("operate" , "check");
        retJSON.put("content" , true);

        Account acct = accts.get(0);
        String mobile = acct.getMobile();
        PortInfo portInfo = portInfoService.findByPhonNum(mobile);
        SendingSMS sendingSMS = new SendingSMS();
        sendingSMS.setPhoNum(mobile);
        sendingSMS.setPortNum(portInfo.getPortNum());
        sendingSMS.setSmsContent(smsContent);
        sendingSMS.setSmsNumber(toPhone);
        sendingSMS.setSmsType(0);
        sendingSMS = sendingSMSService.save(sendingSMS, 10 * 60L);
        return Msg.success(retJSON);
    }

    @RequestMapping(path = {"/notify"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Msg notify(String url , String authKey , Integer live){
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(authKey)){
            return Msg.error("参数错误");
        }

        User user = userService.findByAuthKey(authKey);

        if(user == null){
            return Msg.error("非法账户");
        }

        List<Room> rooms = roomService.findByUrlAndUserId(url , user.getId());
        if(CollectionUtils.isEmpty(rooms) || rooms.size() != 1){
            return Msg.error("直播间未配置");
        }
        Room room = rooms.get(0);

        JSONObject retJSON = new JSONObject();
        retJSON.put("operate" , "notify");
        retJSON.put("content" , true);

        Integer now = DateTime.now().getHourOfDay();
        Integer start = room.getStartHour();
        if(start == null || start < 0){
            start = 0 ;
        }
        if (now < start){
            return  Msg.success(retJSON);
        }
        Integer end = room.getEndHour();

        if (end == null || end < 0){
            end = 23;
        }


        if (now > end){
            return  Msg.success(retJSON);
        }

        if (Integer.valueOf(1).equals(live)){
            return  Msg.success(retJSON);
        }


        String mobile = room.getMobile();

        PortInfo  portInfo = portInfoService.findRandPhoneNum();

        String content = room.getRoomName() + " 同学,你的粉丝望眼欲穿的期待你上线 加油 ^_^";
        SendingSMS sendingSMS4Room = new SendingSMS();

        sendingSMS4Room.setPhoNum(portInfo.getPhoNum());
        sendingSMS4Room.setPortNum(portInfo.getPortNum());

        sendingSMS4Room.setSmsContent(content);
        sendingSMS4Room.setSmsNumber(mobile);
        sendingSMS4Room.setSmsType(0);
        sendingSMS4Room = sendingSMSService.save(sendingSMS4Room, 60*60L);

        SendingSMS sendingSMS4User = new SendingSMS();
        mobile = user.getMobile();

        content = "催促  " + room.getRoomName() + " 直播";
        sendingSMS4User.setPhoNum(portInfo.getPhoNum());
        sendingSMS4User.setPortNum(portInfo.getPortNum());

        sendingSMS4User.setSmsContent(content);
        sendingSMS4User.setSmsNumber(mobile);
        sendingSMS4User.setSmsType(0);
        sendingSMS4User = sendingSMSService.save(sendingSMS4User, 60*60L);


        return Msg.success(retJSON);
    }
}
