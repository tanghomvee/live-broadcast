package com.homvee.livebroadcast.web.interceptors;

import com.google.common.base.Strings;
import com.homvee.livebroadcast.common.components.RedisComponent;
import com.homvee.livebroadcast.common.constants.RedisKey;
import com.homvee.livebroadcast.common.constants.SessionKey;
import com.homvee.livebroadcast.common.enums.SeparatorEnum;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.dao.user.model.User;
import com.homvee.livebroadcast.service.acct.AccountService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-29 09:52
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    protected Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisComponent redisComponent;
    @Resource
    private RoomService roomService;
    @Resource
    private AccountService accountService;
    @Resource
    private UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {


        if (!(request instanceof  ServletServerHttpRequest)){
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        HttpSession session = req.getServletRequest().getSession(true);
        if (session == null) {
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }

        Account account = (Account) session.getAttribute(SessionKey.USER);
        if (account != null){
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }

        String acctName = req.getServletRequest().getParameter("acctName");
        if (Strings.isNullOrEmpty(acctName)) {
            LOGGER.error("账户名错误");
            return false;
        }

        String roomUrl = req.getServletRequest().getParameter("roomUrl");
        if (Strings.isNullOrEmpty(roomUrl)) {
            LOGGER.error("房间地址错误");
            return false;
        }

        String authKey = req.getServletRequest().getParameter("authKey");
        if (Strings.isNullOrEmpty(authKey)) {
            LOGGER.error("授权错误");
            return false;
        }

        LOGGER.info("握手完成前:acctName={},roomUrl={},authKey={}" , acctName ,roomUrl,authKey);


        acctName = acctName.trim();
        User user = userService.findByAuthKey(authKey);
        if(user == null){
            LOGGER.error("未授权的用户:authKey={}" ,authKey);
            return false;
        }

        List<Account> accounts = accountService.findByAcctNameAndUserId(acctName , user.getId());
        if(CollectionUtils.isEmpty(accounts) || accounts.size() != 1){
            LOGGER.error("账户不存在:acctName={}" , acctName);
            return false;
        }

        List<Room> rooms = roomService.findByUrlAndUserId(roomUrl , user.getId());
        if(CollectionUtils.isEmpty(rooms) || rooms.size() != 1){
            LOGGER.error("用户不存在此房间:roomUrl={},userId={}" , roomUrl , user.getId());
            return false;
        }

        account = accounts.get(0);
        attributes.put(SessionKey.USER , user);
        attributes.put(SessionKey.ACCOUNT , account);
        attributes.put(SessionKey.ROOM , rooms.get(0));
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        LOGGER.info("创建握手后...");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
