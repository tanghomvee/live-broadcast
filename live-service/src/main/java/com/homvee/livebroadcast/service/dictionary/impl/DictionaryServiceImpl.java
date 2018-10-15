package com.homvee.livebroadcast.service.dictionary.impl;

import com.homvee.livebroadcast.common.enums.YNEnum;
import com.homvee.livebroadcast.dao.dictionary.DictionaryDao;
import com.homvee.livebroadcast.dao.dictionary.model.Dictionary;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.dictionary.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary,Long> implements DictionaryService {

    @Resource
    private DictionaryDao dictionaryDao;



    @Override
    public List<Dictionary> findAll() {
        return dictionaryDao.findByYn(YNEnum.YES.getVal());
    }

}
