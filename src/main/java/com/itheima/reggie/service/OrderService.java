package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.RiderOrder;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderService.java
 * @Description TODO
 * @createTime 2022年01月28日 21:22:00
 */
public interface OrderService extends IService<Orders> {
    Orders submit(Orders orders);

}
