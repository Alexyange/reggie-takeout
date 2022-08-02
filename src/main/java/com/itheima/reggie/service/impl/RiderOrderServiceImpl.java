package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.Rider;
import com.itheima.reggie.entity.RiderOrder;
import com.itheima.reggie.mapper.RiderOrderMapper;
import com.itheima.reggie.service.AddressBookService;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.RiderOrderService;
import com.itheima.reggie.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RiderOrderServiceImpl.java
 * @Description TODO
 * @createTime 2022年02月28日 19:53:00
 */
@Service
@Slf4j
public class RiderOrderServiceImpl extends ServiceImpl<RiderOrderMapper, RiderOrder> implements RiderOrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private RiderService riderService;

    @Override
    public List<RiderOrder> alldata() {
        LambdaQueryWrapper<Orders> lam = new LambdaQueryWrapper<>();
        lam.eq(Orders::getStatus, 2);
        lam.orderByDesc(Orders::getId);
        List<Orders> ordersList = orderService.list(lam);
        List<RiderOrder> riderOrderList = ordersList.stream().map((item) -> {
            RiderOrder riderOrder = new RiderOrder();
            riderOrder.setOrderId(item.getId());
            riderOrder.setAddress2(item.getAddress());
            riderOrder.setAddress1("安徽合肥市黑马程序元素科技");
            if (null == item.getTips()) {
                item.setTips("5");
            }
            riderOrder.setTips(Integer.parseInt(item.getTips()));
            return riderOrder;
        }).collect(Collectors.toList());
        return riderOrderList;
    }

    @Override
    public void addOrder(Long orderId, Long riderId, Integer tips) {
        Orders orders = orderService.getById(orderId);
        RiderOrder riderOrder = new RiderOrder();
        riderOrder.setAddress1("安徽合肥市黑马程序元素科技");
        riderOrder.setAddress2(orders.getAddress());
        riderOrder.setPhone(orders.getPhone());
        riderOrder.setOrderId(orderId);
        riderOrder.setRiderId(riderId);
        riderOrder.setTips(tips);
        riderOrder.setAccomplishTime(LocalDateTime.now());
        riderOrder.setIssueTime(LocalDateTime.now());
        riderOrder.setPreemptTime(orders.getOrderTime());
        orders.setStatus(3);
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        riderOrder.setXpoint(addressBook.getXPoint());
        riderOrder.setYpoint(addressBook.getYPoint());
        this.save(riderOrder);
        orderService.updateById(orders);
    }
}
