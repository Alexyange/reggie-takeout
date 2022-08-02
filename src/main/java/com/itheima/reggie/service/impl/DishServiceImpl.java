package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName DishServiceImpl.java
 * @Description TODO
 * @createTime 2022年01月24日 20:11:00
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        /**
         * 使用list的stream方法，将list数组里的每个元素都加上某一统一的
         * 值，然后再封装好，最后调用方法  根据指定id的值去存储数据
         *
         * @title saveWithFlavor
         * @description
         * @author Alex
         * @updateTime 2022/1/28 15:41
         * @throws
         */

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> lam = new LambdaQueryWrapper<>();
        lam.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(lam);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        Long dishId = dishDto.getId();

        LambdaQueryWrapper<DishFlavor> lam = new LambdaQueryWrapper<>();
        lam.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(lam);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item -> {
            item.setDishId(dishId);
            return item;
        })).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

}
