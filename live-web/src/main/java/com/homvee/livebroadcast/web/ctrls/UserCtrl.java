package com.homvee.livebroadcast.web.ctrls;

import com.homvee.livebroadcast.common.vos.Msg;
import com.homvee.livebroadcast.common.vos.UserVO;
import com.homvee.livebroadcast.dao.user.model.User;
import com.homvee.livebroadcast.service.user.UserService;
import com.homvee.livebroadcast.web.BaseCtrl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:38
 */
@Controller
@RequestMapping(path = "/user")
public class UserCtrl extends BaseCtrl {

   @Resource
   private UserService userService;

   @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg login(String userName , String pwd){

       if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)){
           return Msg.error("参数错误");
       }
       User user = userService.findByUserNameAndPwd(userName , pwd);
       if(user == null){
           return Msg.error("账户不存在");
       }
       UserVO userVO = new UserVO();
       BeanUtils.copyProperties(user ,userVO);
       setUser(userVO);
       return Msg.success(userVO);
   }

}
