package com.homvee.livebroadcast.dao.catg.impl;

import com.homvee.livebroadcast.common.vos.CategoryVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.JpaDaoSupport;
import com.homvee.livebroadcast.dao.catg.CategoryDaoExt;
import com.homvee.livebroadcast.dao.catg.model.Category;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:26
 */
public class CategoryDaoExtImpl extends JpaDaoSupport<Category,Long> implements CategoryDaoExt {
    @Override
    public Pager findByConditions(CategoryVO categoryVO, Pager pager) {
        return null;
    }
}
