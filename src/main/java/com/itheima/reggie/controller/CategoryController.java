package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CategoryController.java
 * @Description TODO
 * @createTime 2022年01月24日 19:42:00
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public RespData<String> save(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return RespData.success("新增分类成功");
    }

    /**
     * @throws
     * @title
     * @description
     * @author Alex
     * @updateTime 2022/1/24 19:45
     */
    @GetMapping("/page")
    public RespData<Page> page(int page, int pageSize) {
        Page<Category> pageinfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> lam = new LambdaQueryWrapper<>();
        lam.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageinfo, lam);
        return RespData.success(pageinfo);
    }

    @GetMapping("/list")
    public RespData<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lam = new LambdaQueryWrapper<>();
        lam.eq(category.getType() != null, Category::getType, category.getType());
        lam.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lam);
        return RespData.success(list);
    }

    @DeleteMapping
    public RespData<String> delete(Long id) {
        log.info("删除分类，id为：{}", id);
        categoryService.remove(id);
        return RespData.success("分类信息删除成功");
    }

    @PutMapping
    public RespData<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return RespData.success("分类信息修改成功");
    }

}
