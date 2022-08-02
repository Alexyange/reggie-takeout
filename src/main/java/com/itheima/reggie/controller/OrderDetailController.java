package com.itheima.reggie.controller;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderDetailController.java
 * @Description TODO
 * @createTime 2022年01月28日 21:27:00
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.dto.StatisticalDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单明细
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dish")
    public RespData<List<StatisticalDto>> getCountDish(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<StatisticalDto> statisticalDtoList = new ArrayList<>();
        List<Map<String, Object>> dateAndDishId = orderDetailService.findByDateAndDishId(beginTime, endTime);
        for (Map<String, Object> map : dateAndDishId) {
            StatisticalDto statisticalDto = new StatisticalDto();
            if (map.get("dish_id") != null) {
                statisticalDto.setName((String) map.get("name"));
                statisticalDto.setValue((BigDecimal) map.get("num"));
                statisticalDtoList.add(statisticalDto);
            }
        }
        return RespData.success(statisticalDtoList);
    }

    @GetMapping("/setmeal")
    public RespData<List<StatisticalDto>> getCountSetmeal(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<StatisticalDto> statisticalDtoList = new ArrayList<>();
        List<Map<String, Object>> dateAndSetmealId = orderDetailService.findByDateAndSetmealId(beginTime, endTime);
        for (Map<String, Object> map : dateAndSetmealId) {
            StatisticalDto statisticalDto = new StatisticalDto();
            if (map.get("setmeal_id") != null) {
                statisticalDto.setName((String) map.get("name"));
                statisticalDto.setValue((BigDecimal) map.get("num"));
                statisticalDtoList.add(statisticalDto);
            }
        }
        return RespData.success(statisticalDtoList);
    }

    @GetMapping("/dishamount")
    public RespData<Double> getDishCountAmount(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        Double dishAmount = orderDetailService.getDishAmount(beginTime, endTime);
        return RespData.success(dishAmount);
    }

    @GetMapping("/setmealamount")
    public RespData<Double> getSetmealCountAmount(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        Double setmealAmount = orderDetailService.getSetmealAmount(beginTime, endTime);
        return RespData.success(setmealAmount);
    }
}
