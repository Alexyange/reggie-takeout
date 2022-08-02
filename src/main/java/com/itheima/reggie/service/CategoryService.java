package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CategoryService.java
 * @Description TODO
 * @createTime 2022年01月24日 19:40:00
 */
public interface CategoryService extends IService<Category> {
    //根据ID删除分类
    public void remove(Long id);
}
