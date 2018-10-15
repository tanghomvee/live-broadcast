package com.homvee.livebroadcast.service.dictionary;

import com.homvee.livebroadcast.dao.dictionary.model.Dictionary;
import com.homvee.livebroadcast.service.BaseService;

import java.util.List;

public interface DictionaryService extends BaseService<Dictionary, Long> {
    /**
     * find all
     * @return
     */
    List<Dictionary> findAll();

}
