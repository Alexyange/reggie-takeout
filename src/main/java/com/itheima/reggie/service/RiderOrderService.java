package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Rider;
import com.itheima.reggie.entity.RiderOrder;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RiderOrderService.java
 * @Description TODO
 * @createTime 2022年02月28日 19:53:00
 */
public interface RiderOrderService extends IService<RiderOrder> {
    List<RiderOrder> alldata();

    void addOrder(Long orderId, Long riderId, Integer tips);

}
