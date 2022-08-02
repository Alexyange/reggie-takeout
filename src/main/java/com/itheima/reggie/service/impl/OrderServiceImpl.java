package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderServiceImpl.java
 * @Description TODO
 * @createTime 2022年01月28日 21:23:00
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    @Override
    @Transactional
    public Orders submit(Orders orders) {
        Long currentId = BaseContext.getCurrentId();

        //查找购物车得数据
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);

        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能进行结算");
        }

        User user = userService.getById(currentId);

        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不饿能下单");
        }
        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);
        //组装订单明细信息
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setAmount(item.getAmount());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setImage(item.getImage());
            orderDetail.setName(item.getName());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setSetmealId(item.getSetmealId());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            if (null != item.getDishId()) {
                Dish dish = dishService.getById(item.getDishId());
                dish.setSaleNum(dish.getSaleNum() + item.getNumber());
                dishService.updateById(dish);
            }
            if (null != item.getSetmealId()) {
                Setmeal setmeal = setmealService.getById(item.getSetmealId());
                setmeal.setSaleNum(item.getNumber() + setmeal.getSaleNum());
                setmealService.updateById(setmeal);
            }
            return orderDetail;
        })).collect(Collectors.toList());
        //组装订单信息
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(1);
        orders.setAmount(new BigDecimal(String.valueOf(new BigDecimal(amount.get()).subtract(orders.getAmount()))));//总金额
        orders.setUserId(currentId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);
        User user1 = new User();
        user1.setIntegral(user.getIntegral() + orders.getAmount().longValue());
        user1.setId(currentId);
        userService.updateById(user1);
        orderDetailService.saveBatch(orderDetails);
        shoppingCartService.remove(lambdaQueryWrapper);
        return orders;
    }
}
