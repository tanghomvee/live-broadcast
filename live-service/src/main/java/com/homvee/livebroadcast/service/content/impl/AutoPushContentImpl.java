package com.homvee.livebroadcast.service.content.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.homvee.livebroadcast.common.components.RedisComponent;
import com.homvee.livebroadcast.common.constants.RedisKey;
import com.homvee.livebroadcast.common.enums.SeparatorEnum;
import com.homvee.livebroadcast.common.enums.WayEnum;
import com.homvee.livebroadcast.common.vos.Msg;
import com.homvee.livebroadcast.common.vos.RspBody;
import com.homvee.livebroadcast.dao.content.model.Content;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.service.content.AutoPushContent;
import com.homvee.livebroadcast.service.content.ContentService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.websocket.WebSocketMsgHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-29 16:30
 */
@Component
public class AutoPushContentImpl  implements AutoPushContent,InitializingBean {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ContentService contentService;
    @Resource
    private RoomService roomService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private WebSocketMsgHandler webSocketMsgHandler;


    private String[] randStrs = new String[]{
//                "^_^ "," ԅ(¯㉨¯ԅ) "," （￢㉨￢）   ","  ٩(♡㉨♡ )۶  ","  ヽ(○^㉨^)ﾉ♪ ","  (╥ ㉨ ╥`)   ","  ҉٩(*^㉨^*)  ",
//                " （≧㉨≦） "," （⊙㉨⊙） "," (๑•́ ㉨ •̀๑) "," ◟(░´㉨`░)◜ ",
//            "·","^","`",".","_","~",",","、","¯","♡","o_o","I","i","|","l"
//            "来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","🐉","🐮","㊣","哇","嘛","高","行","各位斗友,走一波打赏"
            "强","厉害","来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","哇","嘛","高","行","各位斗友,走一波打赏"
    };
    private String[] emots = new String[]{
            "[emot:dy101]", "[emot:dy102]", "[emot:dy103]", "[emot:dy104]", "[emot:dy105]", "[emot:dy106]", "[emot:dy107]", "[emot:dy108]", "[emot:dy109]",
            "[emot:dy110]", "[emot:dy111]", "[emot:dy112]", "[emot:dy113]", "[emot:dy114]", "[emot:dy115]", "[emot:dy116]", "[emot:dy117]", "[emot:dy118]",
            "[emot:dy119]", "[emot:dy120]", "[emot:dy121]", "[emot:dy122]", "[emot:dy123]", "[emot:dy124]", "[emot:dy125]", "[emot:dy126]", "[emot:dy127]",
            "[emot:dy128]", "[emot:dy129]", "[emot:dy130]", "[emot:dy131]", "[emot:dy132]", "[emot:dy133]", "[emot:dy134]", "[emot:dy135]", "[emot:dy136]",
            "[emot:dy137]", "[emot:dy001]", "[emot:dy002]", "[emot:dy003]", "[emot:dy004]", "[emot:dy005]", "[emot:dy006]", "[emot:dy007]", "[emot:dy008]",
            "[emot:dy009]", "[emot:dy010]", "[emot:dy011]", "[emot:dy012]", "[emot:dy013]", "[emot:dy014]", "[emot:dy015]", "[emot:dy016]", "[emot:dy017]"
    };

    private String[] separators =  new String[]{"㊣","🐉","💗","🐮","❀","👑","🌹","👍","👌","✏","🦐","🧜‍","🐱","🚩","🎸","🖖","👄","🐰","🎈","🎉","🎁","🔥","☀","🎵","🎶","🎼","🔫"};
    private String[] punctuations =  new String[]{".","。","!","!!","^_^","♡",".。","o_o","*","㉨","⊙","🎤","🎙","҉","……"};


    @Override
    public void afterPropertiesSet() throws Exception {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    List<Room> rooms =  roomService.findByWay(WayEnum.AUTO.getVal());
                    if (CollectionUtils.isEmpty(rooms)){
                        LOGGER.warn("未配置自动聊天房间" );
                        continue;
                    }

                    int roomNum = rooms.size();
                    Long waitTime = 10*1000L;
                    if (roomNum > 5){
                        waitTime = 5000L;
                    }
                    for (Room room : rooms){

                        List<Content> contents = contentService.findByRoomId(room.getId());
                        if (CollectionUtils.isEmpty(contents)){
                            LOGGER.warn("房间未配置聊天机器人账号:room={}" , room.getRoomName());
                            continue;
                        }
                        Map<Long , Content> acctContent = Maps.newHashMap();
                        for (Content content : contents){
                            acctContent.put(content.getAcctId() , content);
                        }
                        try {
                            for (Long acctId : acctContent.keySet()){
                                Content content = acctContent.get(acctId);
                                String contentStr =  getContent(content.getContent(),acctId ,room);
                                RspBody rspBody = RspBody.initChatBody(contentStr);
                                TextMessage respMsg = new TextMessage(JSONObject.toJSONString(Msg.success(rspBody)));
                                String acctRoomKey = acctId.toString() + SeparatorEnum.UNDERLINE.getVal() + room.getId();
                                webSocketMsgHandler.sendMsg2User(acctRoomKey ,respMsg);
                            }
                            Thread.sleep(waitTime);
                        } catch (Exception e) {
                            LOGGER.error("向房间推送聊天内容异常:room={}" , room.getRoomName(), e);
                        }
                    }
                }
            }
        });
        thread.start();
    }


    private String getContent(String txt, Long acctId , Room room){
        String contentKey = "" , acctKey =  RedisKey.ACCOUNT + SeparatorEnum.MIDDLE_LINE.getVal() + acctId ;
        Long fiveMinutes = 300L;
        Integer maxCnt = 50;
        if (!StringUtils.isEmpty(txt)){
            contentKey = acctKey  + SeparatorEnum.UNDERLINE.getVal() + txt;
            if (!redisComponent.setStrNx(contentKey , fiveMinutes * 12)){
                txt = null;
            }
        }
        //循环获取账号在当前房间的弹幕
        while (StringUtils.isEmpty(txt)){
            Random random = new Random();
            int num = random.nextInt(6);
            if (num < 3){
                num = 3;
            }
            txt = getRandomStr(num , room.getDefaultContent());
            if (txt.length() < maxCnt){
                int cnt = (maxCnt - txt.length()) / emots[0].length();
                if (cnt > 0){
                    txt = txt + getRandomEmotion(cnt);
                }
            }
            contentKey = acctKey +  SeparatorEnum.UNDERLINE.getVal() + txt;
            if (!redisComponent.setStrNx(contentKey , fiveMinutes * 6)){
                txt = null;
            }
        }

        return txt;
    }

    private String getRandomStr(int num , String defaultContent){
        String[] data = randStrs;
        if (!StringUtils.isEmpty(defaultContent)){
            String[] customerData = defaultContent.split(SeparatorEnum.COMMA.getVal());
            if (!ArrayUtils.isEmpty(customerData)){
                data = ArrayUtils.addAll(data , customerData);
            }
        }
        num = data.length > num ? num : data.length;
        Set<String> contents = Sets.newHashSet();
        String rs = SeparatorEnum.COMMA.getVal();
        Random random = new Random();
        while (num > 0){

            String content = data[random.nextInt(data.length)];
            if (contents.contains(content)){
                continue;
            }
            num--;
            contents.add(content);
            String tmpResult = rs + content + separators[random.nextInt(separators.length)];
            if (tmpResult.length() >= 50){
                return  tmpResult;
            }
            rs = tmpResult;
        }

        return rs + punctuations[random.nextInt(punctuations.length)];
    }
    private String getRandomEmotion(int num){
        String rs = "";
        Set<String> emotsTmp = Sets.newHashSet();
        Random random = new Random();
        while (num > 0){
            String emot = emots[random.nextInt(emots.length)];
            if (emotsTmp.contains(emot)){
                continue;
            }
            emotsTmp.add(emot);
            rs = rs + emot;
            num--;
        }

        return rs;
    }
}
