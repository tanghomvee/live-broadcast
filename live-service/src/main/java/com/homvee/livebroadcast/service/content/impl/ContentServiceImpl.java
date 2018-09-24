package com.homvee.livebroadcast.service.content.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.homvee.livebroadcast.common.components.RedisComponent;
import com.homvee.livebroadcast.common.vos.ContentVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.catg.model.Category;
import com.homvee.livebroadcast.dao.content.ContentDao;
import com.homvee.livebroadcast.dao.content.model.Content;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.acct.AccountService;
import com.homvee.livebroadcast.service.catg.CategoryService;
import com.homvee.livebroadcast.service.content.ContentService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:40
 */
@Service("contentService")
public class ContentServiceImpl extends BaseServiceImpl<Content , Long> implements ContentService {

    @Resource
    private ContentDao contentDao;
    @Resource
    private AccountService accountService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;

    @Resource
    private RedisComponent redisComponent;

    private String[] randStrs = new String[]{
//                "^_^ "," ԅ(¯㉨¯ԅ) "," （￢㉨￢）   ","  ٩(♡㉨♡ )۶  ","  ヽ(○^㉨^)ﾉ♪ ","  (╥ ㉨ ╥`)   ","  ҉٩(*^㉨^*)  ",
//                " （≧㉨≦） "," （⊙㉨⊙） "," (๑•́ ㉨ •̀๑) "," ◟(░´㉨`░)◜ ",
//            "·","^","`",".","_","~",",","、","¯","♡","o_o","I","i","|","l"
            "来","哈哈","呀！", "呢","好","哦","吗？","666666","go","888888","9494","关注主播,惊喜连连","送点什么","闹热","火火","🐉","🐮","㊣","哇","嘛","高","行","各位斗友,走一波打赏"
    };
    private String[] emots = new String[]{
            "[emot:dy101]", "[emot:dy102]", "[emot:dy103]", "[emot:dy104]", "[emot:dy105]", "[emot:dy106]", "[emot:dy107]", "[emot:dy108]", "[emot:dy109]",
            "[emot:dy110]", "[emot:dy111]", "[emot:dy112]", "[emot:dy113]", "[emot:dy114]", "[emot:dy115]", "[emot:dy116]", "[emot:dy117]", "[emot:dy118]",
            "[emot:dy119]", "[emot:dy120]", "[emot:dy121]", "[emot:dy122]", "[emot:dy123]", "[emot:dy124]", "[emot:dy125]", "[emot:dy126]", "[emot:dy127]",
            "[emot:dy128]", "[emot:dy129]", "[emot:dy130]", "[emot:dy131]", "[emot:dy132]", "[emot:dy133]", "[emot:dy134]", "[emot:dy135]", "[emot:dy136]",
            "[emot:dy137]", "[emot:dy001]", "[emot:dy002]", "[emot:dy003]", "[emot:dy004]", "[emot:dy005]", "[emot:dy006]", "[emot:dy007]", "[emot:dy008]",
            "[emot:dy009]", "[emot:dy010]", "[emot:dy011]", "[emot:dy012]", "[emot:dy013]", "[emot:dy014]", "[emot:dy015]", "[emot:dy016]", "[emot:dy017]"
    };

    @Override
    public List<Content> save(List<Content> contents) {
        return contentDao.saveAll(contents);
    }

    @Override
    public Content findOne(Long id) {
        Optional<Content> optional = contentDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(ContentVO contentVO, Pager pager) {
        pager = contentDao.findByConditions(contentVO , pager);
        if(pager != null && !CollectionUtils.isEmpty(pager.getData())){

            List<ContentVO> vos = Lists.newArrayList();
            for (Object obj: pager.getData() ){
                String tmp = JSONObject.toJSONString(obj);
                ContentVO vo = JSON.toJavaObject(JSONObject.parseObject(tmp) , ContentVO.class);
                vos.add(vo);

                Category catg = categoryService.findOne(vo.getCatgId());

                if(catg != null){
                    vo.setCatgName(catg.getCatgName());
                    vo.setParentCatgId(catg.getId());
                    if(catg.getParentId() != null){
                        vo.setParentCatgId(catg.getParentId());
                    }
                }


                if(vo.getPreId() != null){
                    Content preContent = this.findOne(vo.getPreId());
                    if(preContent != null){
                        vo.setPreContent(preContent.getContent());
                    }
                }

            }
            pager.setData(vos);
        }
        return pager;
    }



    /**
     * 如果访问量大需要优化
     * @param roomId
     * @param userId
     * @param account
     * @return
     */
    @Override
    public synchronized Content autoContent(Long roomId, Long userId, Account account) {
        List<Content> contents = contentDao.findByRoomIdAndAcctIdAndUserId(roomId,account.getId() , userId);
        if(CollectionUtils.isEmpty(contents)){
            return null;
        }

        Content content = contents.get(0);

        String roomKey = "room-" + roomId;
        String val = account.getId().toString();

        Long minutes5 = 300L;
        Long max5 = System.currentTimeMillis() - minutes5*1000;
        redisComponent.removeZSetValByScore(roomKey , 0L , max5 );

        LinkedHashSet<String> vals = redisComponent.getZSetVal(roomKey , 0L ,Long.valueOf(Integer.MAX_VALUE));
        int len = vals.size();
        int maxIndex = len > 3 ? len / 3 : 0;
        if(maxIndex >=3 ){
            maxIndex = 3;
        }
        redisComponent.expire(roomKey , 3600L);
        if(Lists.newArrayList(vals).indexOf(val) > maxIndex){
            return null;
        }
        redisComponent.addZSet(roomKey , val , System.currentTimeMillis());
        Long count = redisComponent.incr(roomKey + "-" + val , minutes5);
        Random random = new Random();
        int nums = random.nextInt(6);
        if (nums < 3){
            nums = 3;
        }
        String retEmot = StringUtils.isEmpty(content.getContent()) ? content.getContent() : "";
        retEmot = retEmot + getRandomEmotion(nums) ;
        if(count != null && count % 10 == 0){
            retEmot = retEmot + getRandomStr(nums);
        }
        content.setContent(retEmot);
        return content;
    }


    @Override
    public synchronized Content nextContent(Long roomId, Long userId, Long accountId) {
        Content content = contentDao.findByRoomIdAndUserIdAndAndNotUsed(roomId,userId,accountId);
        if(content == null){
            return null;
        }
        Long preId = content.getPreId();
        if(preId != null && preId > 0){
            Content contentTmp = this.findOne(preId);
            if(contentTmp != null && contentTmp.getUsed() == 0){
                return null;
            }
        }
        content.setRecentUsedTime(new Date());
        content.setUsed(1);
        contentDao.save(content);
        return content;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByIds(Long[] ids) {
        for (Long id : ids){
            contentDao.deleteById(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized Content loopContent(Long roomId, Long userId, Long acctId) {
        Integer cntUsed = contentDao.countUsedByRoomId(roomId);
        Integer cnt = contentDao.countByRoomId(roomId);
        if (cnt.equals(cntUsed)){
            contentDao.resetUsed(roomId);
        }
        Content retContent = null;
        Content content = nextContent(roomId,  userId, acctId);
        if (content != null){
            retContent = new Content();
            retContent.setContent(content.getContent() + getRandomStr(2) + getRandomEmotion(2));
        }
        return retContent;
    }

    @Override
    public List<BigInteger> findAcctByRoomId(Long roomId) {
        return contentDao.findAcctByRoomId(roomId);
    }


    private String getRandomStr(int nums){
        String rs = ",";
        Random random = new Random();
        for (int i = 0 ; i < nums ; i++){
            rs = rs + randStrs[random.nextInt(randStrs.length)];
        }
        return rs;
    }
    private String getRandomEmotion(int nums){
        String rs = "";
        Random random = new Random();
        for (int i = 0 ; i < nums ; i++){
            rs = rs + emots[random.nextInt(emots.length)];
        }
        return rs;
    }

}
