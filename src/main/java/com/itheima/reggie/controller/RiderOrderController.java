package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.Rider;
import com.itheima.reggie.entity.RiderOrder;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.RiderOrderService;
import com.itheima.reggie.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RiderOrderController.java
 * @Description TODO
 * @createTime 2022年02月28日 19:43:00
 */
@RestController
@RequestMapping("/riderorder")
@Slf4j
public class RiderOrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private RiderOrderService riderOrderService;

    @Autowired
    private RiderService riderService;

    @GetMapping("/alldata")
    public RespData<List<RiderOrder>> alldata() {
        List<RiderOrder> riderOrderList = riderOrderService.alldata();
        return RespData.success(riderOrderList);
    }

    @GetMapping("/tomeal/{id}")
    public RespData<String> tomeal(@PathVariable("id") Long id) {
        RiderOrder riderOrder = new RiderOrder();
        riderOrder.setStatus(2);
        LambdaQueryWrapper<RiderOrder> lam = new LambdaQueryWrapper<>();
        lam.eq(RiderOrder::getOrderId, id);
        riderOrderService.update(riderOrder, lam);
        return RespData.success("成功");
    }


    @GetMapping("/tocomplete/{id}")
    public RespData<String> tocomplete(@PathVariable("id") Long id) {
        RiderOrder riderOrder = new RiderOrder();
        riderOrder.setStatus(3);
        Orders orders = orderService.getById(id);
        Long currentId = BaseContext.getCurrentId();
        Rider rider = riderService.getById(currentId);
        if (orders.getTips() == null) {
            orders.setTips("5");
        }
        rider.setBalance(rider.getBalance() + Double.parseDouble(orders.getTips()));
        riderService.updateById(rider);
        LambdaQueryWrapper<RiderOrder> lam = new LambdaQueryWrapper<>();
        lam.eq(RiderOrder::getOrderId, id);
        riderOrderService.update(riderOrder, lam);
        return RespData.success("成功");
    }

    @GetMapping("/{orderId}")
    public RespData<RiderOrder> getOrderById(@PathVariable("orderId") Long orderId) {
        LambdaQueryWrapper<RiderOrder> lam = new LambdaQueryWrapper<>();
        lam.eq(RiderOrder::getOrderId, orderId);
        RiderOrder riderOrder = riderOrderService.getOne(lam);
        return RespData.success(riderOrder);
    }

    @PostMapping("/alldataByrider")
    public RespData<List<RiderOrder>> alldataByrider(@RequestBody Rider rider) {
        LambdaQueryWrapper<RiderOrder> lam = new LambdaQueryWrapper<>();
        lam.eq(RiderOrder::getRiderId, rider.getId());
        lam.orderByDesc(RiderOrder::getId);
        List<RiderOrder> riderOrderList = riderOrderService.list(lam);
        return RespData.success(riderOrderList);
    }

    @PostMapping("/add")
    public RespData<String> add(@RequestBody Map map) {
        Long orderId = Long.parseLong(map.get("orderId").toString());
        Long riderId = Long.parseLong(map.get("riderId").toString());
        Integer tips = Integer.parseInt(map.get("tips").toString());
        riderOrderService.addOrder(orderId, riderId, tips);
        return RespData.success("抢单成功");
    }

    @GetMapping("/getcompletesum")
    public RespData<Integer> getcompletesum() {
        Long currentId = BaseContext.getCurrentId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("rider_id", currentId);
        queryWrapper.select("count(*) as sum");
        Map map = riderOrderService.getMap(queryWrapper);
        Integer sum = Integer.parseInt(map.get("sum").toString());
        return RespData.success(sum);
    }
}
