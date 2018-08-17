package com.homvee.livebroadcast.service;

import com.homvee.livebroadcast.common.vos.Pager;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * base service
 * @param <T>
 * @param <PK>
 */
public interface BaseService<T  , PK extends Serializable>  {

    /**
     * @Description：将org.springframework.data.domain.Page转为Pager
     * @param page
     * @return
     */
     Pager convertPage2Pager(Page page);
}
