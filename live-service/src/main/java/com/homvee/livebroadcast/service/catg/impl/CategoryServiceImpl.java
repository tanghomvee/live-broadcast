package com.homvee.livebroadcast.service.catg.impl;

import com.homvee.livebroadcast.common.vos.CategoryVO;
import com.homvee.livebroadcast.common.vos.Pager;
import com.homvee.livebroadcast.dao.catg.CategoryDao;
import com.homvee.livebroadcast.dao.catg.model.Category;
import com.homvee.livebroadcast.service.BaseServiceImpl;
import com.homvee.livebroadcast.service.catg.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-17 14:24
 */
@Service("categoryService")
public class CategoryServiceImpl extends BaseServiceImpl<Category , Long> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public List<Category> save(List<Category> categories) {
        return categoryDao.saveAll(categories);
    }

    @Override
    public Category findOne(Long id) {
        Optional<Category> optional = categoryDao.findById(id);
        return optional != null && optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Pager findByConditions(CategoryVO categoryVO, Pager pager) {
        return categoryDao.findByConditions(categoryVO , pager);
    }
}
