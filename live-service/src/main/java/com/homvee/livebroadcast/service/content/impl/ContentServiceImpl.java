package com.homvee.livebroadcast.service.content.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.homvee.livebroadcast.common.vos.ContentVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.acct.model.Account;
import com.homvee.livebroadcast.dao.catg.model.Category;
import com.homvee.livebroadcast.dao.content.ContentDao;
import com.homvee.livebroadcast.dao.content.model.Content;
import com.homvee.livebroadcast.dao.room.model.Room;
import com.homvee.livebroadcast.dao.user.model.User;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.acct.AccountService;
import com.homvee.livebroadcast.service.catg.CategoryService;
import com.homvee.livebroadcast.service.content.ContentService;
import com.homvee.livebroadcast.service.room.RoomService;
import com.homvee.livebroadcast.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    private String[] randStrs = new String[]{
//                "^_^ "," ԅ(¯㉨¯ԅ) "," （￢㉨￢）   ","  ٩(♡㉨♡ )۶  ","  ヽ(○^㉨^)ﾉ♪ ","  (╥ ㉨ ╥`)   ","  ҉٩(*^㉨^*)  ",
//                " （≧㉨≦） "," （⊙㉨⊙） "," (๑•́ ㉨ •̀๑) "," ◟(░´㉨`░)◜ ",

            "·","^","`",".","_","~",",","、","¯","♡","o_o","I","i","|","l"
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
     * @return
     */
    @Override
    public synchronized Content nextContent(Long roomId, Long userId) {
        Content content = contentDao.findByRoomIdAndUserId(roomId,userId);
        if(content == null){
            return null;
        }
        Long preId = content.getPreId();
        if(preId != null && preId > 0){
            Content contentTmp = this.findOne(preId);
            if (contentTmp != null){
                if(contentTmp.getRecentUsedTime() == null){
                    return null;
                }
                if(content.getRecentUsedTime() != null && contentTmp.getRecentUsedTime().getTime() < content.getRecentUsedTime().getTime()){
                    return null;
                }
            }
        }
        content.setRecentUsedTime(new Date());
        contentDao.save(content);
        content.setContent(content.getContent() + getRandomStr());
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


    private String getRandomStr(){
        String data  = "" + (System.currentTimeMillis() /1000);
        String rs = "";
        for (int i = 0 , len = data.length(); i < len ; i++){
            int code =Character.getNumericValue(data.toCharArray()[i]);
            rs = rs + randStrs[code];
        }
        return rs;
    }
}
