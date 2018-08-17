package com.homvee.livebroadcast.dao.catg;

import com.homvee.livebroadcast.dao.catg.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-16 11:14
 */
public interface CategoryDao extends JpaRepository<Category, Long> , CategoryDaoExt{
}
