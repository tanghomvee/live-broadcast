package com.homvee.livebroadcast.web.ctrls;

import com.google.common.collect.Lists;
import com.homvee.livebroadcast.common.enums.EncryptionEnum;
import com.homvee.livebroadcast.common.vos.BaseVO;
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
   public Msg login(String userName , String pwd ){

       if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)){
           return Msg.error("参数错误");
       }
       try {
           pwd = EncryptionEnum.RSA.decode(pwd);
           pwd = EncryptionEnum.MD5_2_BASE64.encrypt(pwd);
       } catch (Exception e) {
           LOGGER.error("解密错误:pwd={}" , pwd ,e);
           return Msg.error("密码错误");
       }

       User user = userService.findByUserNameAndPwd(userName , pwd);
       if(user == null){
           return Msg.error("账户不存在");
       }
       UserVO userVO = new UserVO();
       BeanUtils.copyProperties(user ,userVO , BaseVO.getIgnoreProperties());
       userVO.setPwd(null);
       setUser(userVO);
       return Msg.success(userVO);
   }
   @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg setting(String userName ,String oldPwd, String newPwd , String reNewPwd){

       if(StringUtils.isEmpty(userName) ||StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(reNewPwd)){
           return Msg.error("参数错误");
       }

       String pwd = null;
       try {
           newPwd = EncryptionEnum.RSA.decode(newPwd);
           reNewPwd = EncryptionEnum.RSA.decode(reNewPwd);
           if (!newPwd.equals(reNewPwd)){
               return Msg.error("两次输入的密码不一致");
           }
           newPwd = EncryptionEnum.MD5_2_BASE64.encrypt(newPwd);

           pwd = EncryptionEnum.RSA.decode(oldPwd);
           pwd = EncryptionEnum.MD5_2_BASE64.encrypt(pwd);
       } catch (Exception e) {
           LOGGER.error("解密错误:pwd={}" , pwd ,e);
           return Msg.error("密码错误");
       }



       User user = userService.findByUserNameAndPwd(userName , pwd);
       if(user == null){
           return Msg.error("账户不存在");
       }
       user.setPwd(newPwd);
       userService.save(Lists.newArrayList(user));

       UserVO userVO = new UserVO();
       BeanUtils.copyProperties(user ,userVO , BaseVO.getIgnoreProperties());
       userVO.setPwd(null);
       setUser(userVO);
       return Msg.success(userVO);
   }

}
