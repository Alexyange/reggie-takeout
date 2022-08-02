package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName ShoppingCartController.java
 * @Description TODO 购物车控制类
 * @createTime 2022年01月28日 21:01:00
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    //完善功能  移除购物车商品
    @PostMapping("/sub")
    public RespData<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据:{}", shoppingCart);

        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
        if (one != null) {
            Integer number = one.getNumber();
            if ((number - 1) == 0) {
                shoppingCartService.remove(lambdaQueryWrapper);
            }
            one.setNumber(number - 1);
            shoppingCartService.updateById(one);
        }
        return RespData.success(one);
    }

    @PostMapping("/add")
    public RespData<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据:{}", shoppingCart);

        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
        if (one != null) {
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return RespData.success(one);
    }

    @GetMapping("/list")
    public RespData<List<ShoppingCart>> list() {
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> lam = new LambdaQueryWrapper<>();
        lam.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lam.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lam);
        return RespData.success(shoppingCarts);
    }

    @DeleteMapping("/clean")
    public RespData<String> remove() {
        log.info("清空购物车...");
        LambdaQueryWrapper<ShoppingCart> lam = new LambdaQueryWrapper<>();
        lam.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lam);
        return RespData.success("清空购物车成功");
    }
}
