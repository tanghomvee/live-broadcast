package com.homvee.livebroadcast.web.ctrls;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.homvee.livebroadcast.common.enums.WayEnum;
import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.common.vos.BaseVO;
import com.homvee.livebroadcast.common.vos.ContentVO;
import com.homvee.livebroadcast.common.vos.Msg;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.content.model.Content;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.dao.user.model.User;
import com.homvee.livebroadcast.service.acct.AccountService;
import com.homvee.livebroadcast.service.content.ContentService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.user.UserService;
import com.homvee.livebroadcast.web.BaseCtrl;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 10:38
 */
@Controller
@RequestMapping(path = "/content")
public class ContentCtrl extends BaseCtrl {

   @Resource
   private ContentService contentService;

    @Resource
    private RoomService roomService;

    @Resource
    private AccountService accountService;

    @Resource
    private UserService userService;

    private LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
            //最多存放1000个数据
            .maximumSize(1000)
            //缓存1H，1H之后进行回收
            .expireAfterWrite(8, TimeUnit.HOURS)
            //开启，记录状态数据功能
            .recordStats()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(10)
            .build(new CacheLoader<String, Long>() {
                //数据加载，默认返回-1，也可以是查询操作，如从DB查询
                @Override
                public Long load(String key) throws Exception {
                    // TODO Auto-generated method stub
                    return -1L;
                }
            });
    private LoadingCache<String, ConcurrentLinkedQueue> cacheLinkedQueue = CacheBuilder.newBuilder()
            //最多存放1000个数据
            .maximumSize(1000)
            //缓存1H，1H之后进行回收
            .expireAfterWrite(5, TimeUnit.MINUTES)
            //开启，记录状态数据功能
            .recordStats()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(10)
            .build(new CacheLoader<String, ConcurrentLinkedQueue>() {
                //数据加载，默认返回-1，也可以是查询操作，如从DB查询
                @Override
                public ConcurrentLinkedQueue load(String key) throws Exception {
                    // TODO Auto-generated method stub
                    List<BigInteger> accts = contentService.findAcctByRoomId(Long.valueOf(key));
                    ConcurrentLinkedQueue<Long> retLinked =  new ConcurrentLinkedQueue();
                    if (!CollectionUtils.isEmpty(accts)){
                        for (BigInteger acct : accts){
                            retLinked.add(acct.longValue());
                        }
                    }
                    return retLinked;
                }
            });

   @RequestMapping(path = {"/add"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg save(ContentVO contentVO){
       
       if(StringUtils.isEmpty(contentVO.getCatgId()) || contentVO.getCatgId() < 1
               || StringUtils.isEmpty(contentVO.getAcctId()) || contentVO.getAcctId() < 1
               || StringUtils.isEmpty(contentVO.getRoomId()) || contentVO.getRoomId() < 1){
           return Msg.error("参数错误");
       }

       Content content = new Content();
       BeanUtils.copyProperties(contentVO ,content);
       content.setUsed(0);
       content.setUserId(getUser().getId());
       content.setCreator(getUser().getUserName());
       content.setYn(YNEnum.YES.getVal());
       contentService.save(Lists.newArrayList(content));
       return Msg.success();
   }
   @RequestMapping(path = {"/list"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg list(ContentVO contentVO, Pager pager){
       contentVO.setUserId(getUser().getId());
       pager = contentService.findByConditions(contentVO , pager);
       return Msg.success(pager);
   }

   @RequestMapping(path = {"/one"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg findOne(Long id){
       if(StringUtils.isEmpty(id) || id < 1){
           return Msg.error("参数错误");
       }
       Content content = contentService.findOne(id);
       if(content == null){
           return Msg.error("直播内容不存在");
       }
       ContentVO contentVO = new ContentVO();
       BeanUtils.copyProperties(content ,contentVO);
       return Msg.success(contentVO);
   }
   @RequestMapping(path = {"/del"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg del(Long[] ids){
       if(StringUtils.isEmpty(ids) || ids.length < 1){
           return Msg.error("参数错误");
       }
       contentService.delByIds(ids);
       cacheLinkedQueue.cleanUp();
       return Msg.success();
   }

   @RequestMapping(path = {"/edit"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public Msg edit(ContentVO contentVO){
       if(StringUtils.isEmpty(contentVO) || contentVO.getId() < 1){
           return Msg.error("参数错误");
       }
       Content content = contentService.findOne(contentVO.getId());
       if(content == null){
           return Msg.error("直播内容不存在");
       }

       BeanUtils.copyProperties(contentVO ,content , ArrayUtils.add(BaseVO.getIgnoreProperties() , "userId"));
       content.setChanger(getUser().getUserName());
       content.setChangeTime(new Date());
       contentService.save(Lists.newArrayList(content));
       return Msg.success();
   }



    /**
     * {
     * 	"content":"发言内容或者URL",
     * 	"operate":"操作类型"//chat:发言标志;wait:等待发言;go:跳转到指定的房间此时content的数据为URL
     * }
     * @param url
     * @param acctName
     * @param authKey
     */

   @RequestMapping(path = {"/chat"}, method = {RequestMethod.GET, RequestMethod.POST})
   @ResponseBody
   public synchronized Msg chat(String url , String acctName , String authKey){


       if(StringUtils.isEmpty(url) || StringUtils.isEmpty(acctName) || StringUtils.isEmpty(authKey)){
           return Msg.error("参数错误");
       }
       acctName = acctName.trim();

       User user = userService.findByAuthKey(authKey);

       if(user == null){
           return Msg.error("非法账户");
       }

       List<Room> rooms = roomService.findByUrlAndUserId(url , user.getId());
       if(CollectionUtils.isEmpty(rooms) || rooms.size() != 1){
           return Msg.error("直播间未配置");
       }

       Room room = rooms.get(0);
       String operate = "wait" ;
       Object txt = 10000;
       JSONObject retJSON = new JSONObject();
       if (WayEnum.STOP.getVal().equals(room.getWay())){
           retJSON.put("operate" , operate);
           retJSON.put("content" , txt);
           return Msg.success(retJSON);
       }


       Long interval = room.getIntervalTime();

       if(interval != null ){
           Long lastTime = getRoomChatInterval(room.getId());
           if (lastTime != null){
               interval = interval * 1000;
               Long deltaSeconds = System.currentTimeMillis() - lastTime;
               if(deltaSeconds < interval){
                   retJSON.put("operate" , operate);
                   retJSON.put("content" , interval - deltaSeconds);
                   return Msg.success(retJSON);
               }
           }
       }


       List<Account> accounts = accountService.findByAcctNameAndUserId(acctName , user.getId());
       if(CollectionUtils.isEmpty(accounts) || accounts.size() != 1){
           return Msg.error("账户不存在");
       }

       Content content = null;
       if (WayEnum.AUTO.getVal().equals(room.getWay())){
           Account account = accounts.get(0);
           Long acctId = account.getId();
           ConcurrentLinkedQueue<Long> linkedQueue = null;
           try {
               linkedQueue = cacheLinkedQueue.get(room.getId().toString());
               if (!linkedQueue.isEmpty()){
                   if(linkedQueue.contains(acctId)){
                       if(!linkedQueue.element().equals(acctId)){
                           retJSON.put("operate" , operate);
                           retJSON.put("content" , txt);
                           return Msg.success(retJSON);
                       }
                       acctId = linkedQueue.poll();
                   }
               }
               linkedQueue.offer(acctId);
               cacheLinkedQueue.put(room.getId().toString() , linkedQueue);
           } catch (ExecutionException e) {
               LOGGER.error("" , e);
               retJSON.put("operate" , operate);
               retJSON.put("content" , txt);
               return Msg.success(retJSON);
           }

           content = contentService.autoContent(room.getId() , user.getId());
       }else {

           Account account = accounts.get(0);
           if(WayEnum.NORMAL.getVal().equals(room.getWay())){
               content = contentService.nextContent(room.getId() , user.getId() , account.getId());
           }else{
               content = contentService.loopContent(room.getId() , user.getId() , account.getId());
           }
       }




       if (content != null){
           operate = "chat";
           txt = content.getContent();
           if(interval != null ){
               setRoomChatInterval(room.getId() , System.currentTimeMillis());
           }
       }

       retJSON.put("operate" , operate);
       retJSON.put("content" , txt);
       return Msg.success(retJSON);
   }

    private void setRoomChatInterval(Long roomId ,Long time) {
        cache.put(roomId.toString() , time);
    }
    private Long getRoomChatInterval(Long roomId) {
        try {
            return cache.get(roomId.toString());
        } catch (ExecutionException e) {
            LOGGER.error("" , e);
        }

        return -1L;
    }
}
