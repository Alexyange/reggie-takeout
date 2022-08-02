package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName DishController.java
 * @Description TODO
 * @createTime 2022年01月28日 14:45:00
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    public RespData<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return RespData.success("新增菜品成功");
    }

    //完善功能1  批量或者单个停售菜品状态
    @PostMapping("/status/0")
    public RespData<String> status0(@RequestParam List<Long> ids) {
        log.info("开始停售菜品");
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setStatus(0);
            dish.setId(id);
            dishService.updateById(dish);
        }
        return RespData.success("菜品停售成功");
    }

    //完善功能2  批量或者单个启售菜品状态
    @PostMapping("/status/1")
    public RespData<String> status1(@RequestParam List<Long> ids) {
        log.info("开始启售菜品");
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setStatus(1);
            dish.setId(id);
            dishService.updateById(dish);
        }
        return RespData.success("菜品停售成功");
    }

    //完善功能3  批量或者单个删除菜品状态
    @DeleteMapping
    public RespData<String> delete(@RequestParam List<Long> ids) {
        log.info("开始删除菜品");
//        for (Long id : ids) {
//            Dish dish = dishService.getById(id);
//            String key = "dish_" + dish.getCategoryId() + "_1";
//            redisTemplate.delete(key);
//            dishService.removeById(id);
//        }

        //遍历传递过来的数组
        // 条件构造器删除 口味数据
        //完事之后再调用方法删除基本的菜品数
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = dishService.count(lqw);
        if (count > 0) {
            throw new CustomException("菜品在售卖");
        }
        dishService.removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(qw);


        return RespData.success("删除菜品成功");
    }

    @GetMapping("/page")
    public RespData<Page> page(int page, int pageSize, String name) {
        Page<Dish> info = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Dish> lam = new LambdaQueryWrapper<>();
        lam.like(name != null, Dish::getName, name);
        lam.orderByDesc(Dish::getUpdateTime);

        dishService.page(info, lam);

        //将info里的数据拷贝到dtopage里,同时忽略records字段拷贝
        BeanUtils.copyProperties(info, dtoPage, "records");
        List<Dish> records = info.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return RespData.success(dtoPage);
    }

    @GetMapping("/{id}")
    public RespData<DishDto> findById(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return RespData.success(dishDto);
    }

    @PutMapping
    public RespData<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return RespData.success("菜品更新成功");
    }

    @GetMapping("/list")
    public RespData<List<DishDto>> list(Dish dish) {
        List<DishDto> dtos = null;
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        dtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (null != dtos) {
            return RespData.success(dtos);
        }
        LambdaQueryWrapper<Dish> lam = new LambdaQueryWrapper<>();
        lam.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lam.eq(Dish::getStatus, 1);
        lam.orderByDesc(Dish::getUpdateTime).orderByAsc(Dish::getSort);
        List<Dish> list = dishService.list(lam);
        dtos = list.stream().map((item) -> {
            //赋值给name字段
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null)
                dishDto.setCategoryName(category.getName());
            //查找flavor的数据，然后将其set到dishdto里面
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> list1 = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(list1);
            //最后返回个DishDto
            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key, dtos, 60, TimeUnit.MINUTES);
        return RespData.success(dtos);

    }
}
