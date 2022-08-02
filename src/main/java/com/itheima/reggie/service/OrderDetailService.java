package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderDetailService.java
 * @Description TODO
 * @createTime 2022年01月28日 21:23:00
 */
public interface OrderDetailService extends IService<OrderDetail> {

    List<Map<String, Object>> findByDateAndDishId(Date beginTime, Date endTime);

    List<Map<String, Object>> findByDateAndSetmealId(Date beginTime, Date endTime);

    Double getDishAmount(Date beginTime, Date endTime);

    Double getSetmealAmount(Date beginTime, Date endTime);
}
