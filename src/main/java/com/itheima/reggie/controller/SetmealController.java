package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName SetmealController.java
 * @Description TODO
 * @createTime 2022年01月28日 17:06:00
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    CategoryMapper categoryMapper;

    //完善功能4  批量或者单个停售套餐状态
    @PostMapping("/status/0")
    public RespData<String> status0(@RequestParam List<Long> ids) {
        log.info("开始停售菜品");
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(0);
            setmeal.setId(id);
            setmealService.updateById(setmeal);
        }
        return RespData.success("菜品停售成功");
    }

    //完善功能5  批量或者单个启售套餐状态
    @PostMapping("/status/1")
    public RespData<String> status1(@RequestParam List<Long> ids) {
        log.info("开始启售菜品");
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(1);
            setmeal.setId(id);
            setmealService.updateById(setmeal);
        }
        return RespData.success("菜品停售成功");
    }

    //完善功能6 修改套餐信息时回显套餐信息
    @GetMapping("/{id}")
    public RespData<SetmealDto> findById(@PathVariable("id") Long id) {
        log.info("回显套餐：{}", id);

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        return RespData.success(setmealDto);
    }

    @GetMapping("/dish/{id}")
    public RespData<List<DishDto>> findByid(@PathVariable("id") Long id) {
        log.info("回显套餐：{}", id);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(lambdaQueryWrapper);
        List<DishDto> dishDtos = new ArrayList<>();
        for (int i = 0; i < setmealDishes.size(); i++) {
            Long dishId = setmealDishes.get(i).getDishId();
            Dish byId = dishService.getById(dishId);
            DishDto dishDto = new DishDto();
            dishDto.setCopies(setmealDishes.get(i).getCopies());
            BeanUtils.copyProperties(byId, dishDto);
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(lambdaQueryWrapper1);
            dishDto.setFlavors(dishFlavors);
            Category category = categoryMapper.selectById(byId.getCategoryId());
            dishDto.setCategoryName(category.getName());
            dishDtos.add(dishDto);
        }
        return RespData.success(dishDtos);
    }

    //完善功能7 修改套餐数据传到数据库
    @PutMapping
    public RespData<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);

        setmealService.updateWithDish(setmealDto);

        return RespData.success("新增套餐成功");
    }

    @PostMapping
    public RespData<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);

        setmealService.saveWithDish(setmealDto);

        return RespData.success("新增套餐成功");
    }

    @DeleteMapping
    public RespData<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);
        return RespData.success("套餐数据删除成功");
    }


    @GetMapping("/page")
    public RespData<Page> page(Integer page, Integer pageSize, String name) {
        Page<Setmeal> info = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> lam = new LambdaQueryWrapper<>();
        lam.like(name != null, Setmeal::getName, name);
        lam.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(info, lam);
        BeanUtils.copyProperties(info, dtoPage, "records");
        List<Setmeal> records = info.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return RespData.success(dtoPage);
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public RespData<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return RespData.success(list);
    }
}
