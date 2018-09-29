package com.homvee.livebroadcast.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.homvee.livebroadcast.common.components.RedisComponent;
import com.homvee.livebroadcast.common.constants.RedisKey;
import com.homvee.livebroadcast.common.constants.SessionKey;
import com.homvee.livebroadcast.common.enums.OperatorEnum;
import com.homvee.livebroadcast.common.enums.SeparatorEnum;
import com.homvee.livebroadcast.common.vos.Msg;
import com.homvee.livebroadcast.common.vos.ReqBody;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.dao.sms.model.PortInfo;
import com.homvee.livebroadcast.dao.sms.model.SendingSMS;
import com.homvee.livebroadcast.service.acct.AccountService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.sms.PortInfoService;
import com.homvee.livebroadcast.service.sms.SendingSMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description web socket 消息处理类
 * @date 2018-09-29 09:48
 */
@Component("socketMsgHandler")
public class WebSocketMsgHandler extends TextWebSocketHandler {

    protected Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

    private Map<String , WebSocketSession> users = Maps.newConcurrentMap();

    @Resource
    private AccountService accountService;
    @Resource
    private SendingSMSService sendingSMSService;
    @Resource
    private PortInfoService portInfoService;

    @Resource
    private RoomService roomService;
    @Resource
    private RedisComponent redisComponent;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("连接建立后处理方法");
        Map<String , Object> attrs = session.getAttributes();
        if(CollectionUtils.isEmpty(attrs)){
            LOGGER.error("连接成功获取账号信息失败");
            return;
        }
        Account account = (Account) attrs.get(SessionKey.USER);
        if(account == null){
            LOGGER.error("连接成功获取账号信息不存在");
            return;
        }
        users.put(account.getId().toString() , session);
        super.afterConnectionEstablished(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("处理前台推送消息{}" , message.toString());
        Map<String , Object> attrs = session.getAttributes();
        if (CollectionUtils.isEmpty(attrs)){
            LOGGER.error("非法客户推送的消息:{}" , message);
            return;
        }
        Long userId = (Long) attrs.get("userId");
        if (userId == null){
            LOGGER.error("非法用户推送的消息:{}" , message);
            return;
        }

        JSONObject jsonData = JSONObject.parseObject(message.getPayload());
        ReqBody reqBody = JSONObject.toJavaObject(jsonData , ReqBody.class);
        String acctName = reqBody.getAcctName();
        //发送验证短信
        if (OperatorEnum.SMS_CHECK.getVal().equals(reqBody.getOperate())){

            String sms = reqBody.getCheckSMS().getContent();
            String toPhone = reqBody.getCheckSMS().getToPhone();

            if (StringUtils.isEmpty(sms) || StringUtils.isEmpty(toPhone) || StringUtils.isEmpty(acctName)){
                LOGGER.error("发送验证短信参数错误:sms={},toPhone={},acctName={}" , sms ,toPhone ,acctName);
                return;
            }
            Msg msg = this.sendCheckMsg(acctName,toPhone,sms ,userId);
            if (msg.isSuccess()){
                TextMessage respMsg = new TextMessage(JSONObject.toJSONString(msg));
                session.sendMessage(respMsg);
            }
        }else if (OperatorEnum.ROOM_CHECK.getVal().equals(reqBody.getOperate())){
            String roomUrl = reqBody.getCheckRoom().getRoomUrl();
            if (StringUtils.isEmpty(roomUrl)  || StringUtils.isEmpty(acctName)){
                LOGGER.error("房间验证短信参数错误:roomUrl={},acctName={}" , roomUrl ,acctName);
                return;
            }
            Msg msg = this.checkRoom(acctName , roomUrl ,userId);
            if (msg.isSuccess()){
                TextMessage respMsg = new TextMessage(JSONObject.toJSONString(msg));
                session.sendMessage(respMsg);
            }
        }

        super.handleTextMessage(session, message);
    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.info("抛出异常时处理方法");
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("连接关闭后处理方法");
        super.afterConnectionClosed(session, status);
    }


    /**
     * 给某个用户发送消息
     *
     * @param acctRoomKey
     * @param message
     */
    public void sendMsg2User(String acctRoomKey, TextMessage message) {
        WebSocketSession user = users.get(acctRoomKey);
        try {
            if (user == null || !user.isOpen()) {
                LOGGER.info("房间已经断开:{}" , acctRoomKey);
                return;
            }
            user.sendMessage(message);
        } catch (Exception e) {
            LOGGER.error("房间发送消息异常:{}" , acctRoomKey , e);
        }
    }




    private Msg sendCheckMsg(String acctName,  String toPhone , String smsContent , Long userId){

        List<Account> accts = accountService.findByAcctNameAndUserId(acctName.trim() , userId);
        if (CollectionUtils.isEmpty(accts)){
            LOGGER.error("账号不存在:{}" , acctName);
            return Msg.error();
        }
        Account acct = accts.get(0);
        String mobile = acct.getMobile();
        PortInfo portInfo = portInfoService.findByPhonNum(mobile);
        SendingSMS sendingSMS = new SendingSMS();
        sendingSMS.setPhoNum(mobile);
        sendingSMS.setPortNum(portInfo.getPortNum());
        sendingSMS.setSmsContent(smsContent);
        sendingSMS.setSmsNumber(toPhone);
        sendingSMS.setSmsType(0);
        sendingSMS = sendingSMSService.save(sendingSMS, 30 * 60L);

        JSONObject retData = new JSONObject();
        retData.put(OperatorEnum.SMS_CHECK.getVal() , true);
        return Msg.success(retData);
    }
    private Msg checkRoom(String acctName,  String roomUrl , Long userId){

        List<Account> accts = accountService.findByAcctNameAndUserId(acctName.trim() , userId);
        if (CollectionUtils.isEmpty(accts)){
            LOGGER.error("账号不存在:{}" , acctName);
            return Msg.error();
        }
        Account acct = accts.get(0);
        Long acctId = acct.getId();

        List<Room> rooms = roomService.findByUrlAndUserId(roomUrl , userId);
        if(CollectionUtils.isEmpty(rooms) || rooms.size() != 1){
            LOGGER.error("用户不存在此房间:roomUrl={},userId={}" , roomUrl , userId);
            return Msg.error();
        }


        rooms = roomService.findByAcctIdAndUserId(acctId , userId);
        if (CollectionUtils.isEmpty(rooms)){
            LOGGER.error("用户不存在任何房间:roomUrl={},userId={}" , roomUrl , userId);
            return Msg.error();
        }

        String retUrl = null;
        for (Room room : rooms){
            String roomKey = RedisKey.ROOM + SeparatorEnum.UNDERLINE.getVal() + room.getId();
            if (!room.getId().toString().equals(redisComponent.get(roomKey))){
                retUrl = room.getUrl();
                break;
            }
        }

        if (StringUtils.isEmpty(retUrl)){
            return Msg.error();
        }

        JSONObject retData = new JSONObject();
        retData.put(OperatorEnum.ROOM_CHECK.getVal() , retUrl);
        return Msg.success(retData);
    }

}
