package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.mapper.OrderDetailMapper;
import com.itheima.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderDetailServiceImpl.java
 * @Description TODO
 * @createTime 2022年01月28日 21:26:00
 */
@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public List<Map<String, Object>> findByDateAndDishId(Date beginTime, Date endTime) {
        return orderDetailMapper.findByDateAndDishId(beginTime, endTime);
    }

    @Override
    public List<Map<String, Object>> findByDateAndSetmealId(Date beginTime, Date endTime) {
        return orderDetailMapper.findByDateAndSetmealId(beginTime, endTime);
    }

    @Override
    public Double getDishAmount(Date beginTime, Date endTime) {
        Double countamount = 0.0;
        List<Map<String, Object>> dishAmount = orderDetailMapper.getDishAmount(beginTime, endTime);
        for (Map<String, Object> map : dishAmount) {
            Double amount = Double.valueOf(map.get("amount").toString());
            Double number = Double.valueOf(map.get("number").toString());
            countamount += number * amount;
        }
        return countamount;
    }

    @Override
    public Double getSetmealAmount(Date beginTime, Date endTime) {
        Double countamount = 0.0;
        List<Map<String, Object>> dishAmount = orderDetailMapper.getSetmealAmount(beginTime, endTime);
        for (Map<String, Object> map : dishAmount) {
            Double amount = Double.valueOf(map.get("amount").toString());
            Double number = Double.valueOf(map.get("number").toString());
            countamount += number * amount;
        }
        return countamount;
    }

}
