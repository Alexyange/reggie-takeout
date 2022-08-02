package com.itheima.reggie.controller;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderController.java
 * @Description TODO
 * @createTime 2022年01月28日 21:27:00
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.dto.CommentsOrderDto;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CommentsService commentsService;


    @PostMapping("/toupdate")
    public RespData<String> toupdate(@RequestBody Orders orders) {
        orderService.updateById(orders);
        return RespData.success("更改成功");
    }

    @GetMapping("/getorder")
    public RespData<Orders> getOrders(Long id) {
        Orders orders = orderService.getById(id);
        return RespData.success(orders);
    }

    @PutMapping("/tocomplete")
    public RespData<String> toComplete(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orders.setStatus(4);
        orderService.updateById(orders);
        return RespData.success("订单已完成");
    }

    @PutMapping("/tocancel")
    public RespData<String> toCancle(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orders.setStatus(5);
        orderService.updateById(orders);
        return RespData.success("订单已取消");
    }

    @PostMapping("/submit")
    public RespData<Orders> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        Orders order = orderService.submit(orders);
        return RespData.success(order);
    }

    //完善功能8 分页查询订单明细
    @GetMapping("/page")
    public RespData<Page> page(Integer page, Integer pageSize, Long number, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        Page<Orders> pageinfo = new Page<>(page, pageSize);
        Page<CommentsOrderDto> commentsOrderDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> lam = new LambdaQueryWrapper<>();
        lam.like(number != null, Orders::getNumber, number);
        lam.between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime);
        lam.orderByDesc(Orders::getOrderTime);
        orderService.page(pageinfo, lam);
        BeanUtils.copyProperties(pageinfo, commentsOrderDtoPage, "records");
        List<Orders> ordersList = pageinfo.getRecords();
        List<CommentsOrderDto> commentsOrderDtoList = ordersList.stream().map((item) -> {
            CommentsOrderDto commentsOrderDto = new CommentsOrderDto();
            BeanUtils.copyProperties(item, commentsOrderDto);
            LambdaQueryWrapper<Comments> law = new LambdaQueryWrapper<>();
            law.eq(Comments::getOrderId, item.getId());
            List<Comments> commentsList = commentsService.list(law);
            commentsOrderDto.setComments(commentsList);
            return commentsOrderDto;
        }).collect(Collectors.toList());
        commentsOrderDtoPage.setRecords(commentsOrderDtoList);
        return RespData.success(commentsOrderDtoPage);
    }

    //完善功能9 修改订单明状态
    @PutMapping
    public RespData<String> updateStatus(@RequestBody Orders orders) {
        log.info("订单信息更改");
        orderService.updateById(orders);
        return RespData.success("订单状态修改成功");
    }

    //完善功能 再来一单
    @PostMapping("/again")
    public RespData<String> again(@RequestBody Orders orders) {
        log.info("再来一单");
        ShoppingCart shoppingCart = new ShoppingCart();
        LambdaQueryWrapper<OrderDetail> lam = new LambdaQueryWrapper<>();
        lam.eq(OrderDetail::getOrderId, orders.getId());
        List<OrderDetail> orderDetailList = orderDetailService.list(lam);
        Long currentId = BaseContext.getCurrentId();
        for (OrderDetail orderDetail : orderDetailList) {
            shoppingCart.setUserId(currentId);
            Long dishId = orderDetail.getDishId();
            shoppingCart.setAmount(orderDetail.getAmount());
            Long setmealId = orderDetail.getSetmealId();
            LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
            if (dishId != null) {
                lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
                shoppingCart.setDishId(dishId);
                LambdaQueryWrapper<Dish> lamdish = new LambdaQueryWrapper<>();
                lamdish.eq(Dish::getId, dishId);
                Dish dish = dishService.getOne(lamdish);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
            }
            if (setmealId != null) {
                lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
                shoppingCart.setSetmealId(setmealId);
                LambdaQueryWrapper<Setmeal> lamsetmeal = new LambdaQueryWrapper<>();
                lamsetmeal.eq(Setmeal::getId, setmealId);
                Setmeal setmeal = setmealService.getOne(lamsetmeal);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
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
        }

        return RespData.success("订单状态修改成功");
    }

    //用户订单信息展示
    @GetMapping("/userPage")
    public RespData<Page> userPage(Integer page, Integer pageSize) {
        Page<Orders> pageinfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>();
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> lam = new LambdaQueryWrapper<>();
        lam.eq(Orders::getUserId, currentId);
        lam.orderByDesc(Orders::getOrderTime);
        orderService.page(pageinfo, lam);
        BeanUtils.copyProperties(pageinfo, dtoPage, "records");
        List<Orders> records = pageinfo.getRecords();
        List<OrdersDto> record = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getNumber());
            List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            QueryWrapper qw = new QueryWrapper<>();
            qw.select("count(*) as num");
            qw.eq("order_id", ordersDto.getId());
            Map map = commentsService.getMap(qw);
            Long num = (Long) map.get("num");
            ordersDto.setRemark("" + num);
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(record);
        return RespData.success(dtoPage);
    }
}
