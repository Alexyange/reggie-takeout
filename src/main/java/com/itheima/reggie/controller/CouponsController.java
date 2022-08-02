package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.dto.CouponsDTO;
import com.itheima.reggie.entity.Coupons;
import com.itheima.reggie.service.CouponsService;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName IntegralController.java
 * @Description TODO
 * @createTime 2022年02月03日 10:02:00
 */
@RestController
@Slf4j
@RequestMapping("/coupons")
public class CouponsController {

    @Autowired
    private CouponsService couponsService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public RespData<String> addCoupon(String code) {
        String coupons1 = "5wumenkan";
        String coupons2 = "10wumenkan";
        String coupons3 = "15wumenkan";
        String coupons4 = "2wumenkan";
        String coupons5 = "100-10youhuiquan";
        String coupons6 = "50-10youhuiquan";
        Long currentId = BaseContext.getCurrentId();
        Coupons coupons = new Coupons();
        if (coupons1.equals(code)) {
            coupons.setId(IdWorker.getId());
            coupons.setName("5元无门槛优惠券");
            coupons.setDescription("使用规则：\n无门槛立减优惠券");
            coupons.setRules("0-5");
            coupons.setUserid(currentId);
        } else if (coupons2.equals(code)) {
            coupons.setId(IdWorker.getId());
            coupons.setName("10元无门槛优惠券");
            coupons.setDescription("使用规则：\n无门槛立减优惠券");
            coupons.setRules("0-10");
            coupons.setUserid(currentId);
        } else if (coupons3.equals(code)) {
            coupons.setId(IdWorker.getId());
            coupons.setName("15元无门槛优惠券");
            coupons.setDescription("使用规则：\n无门槛立减优惠券");
            coupons.setRules("0-15");
            coupons.setUserid(currentId);
        } else if (coupons4.equals(code)) {
            coupons.setId(IdWorker.getId());
            coupons.setName("2元无门槛优惠券");
            coupons.setDescription("使用规则：\n无门槛立减优惠券");
            coupons.setRules("0-2");
            coupons.setUserid(currentId);
        } else if (coupons5.equals(code)) {
            coupons.setId(IdWorker.getId());
            coupons.setName("满100减10优惠券");
            coupons.setDescription("使用规则：\n订单金额满100元可减10元");
            coupons.setRules("100-10");
            coupons.setUserid(currentId);
        } else if (coupons6.equals(code)) {
            coupons.setId(IdWorker.getId());
            coupons.setName("满50减10优惠券");
            coupons.setDescription("使用规则：\n订单金额满50元可减10元");
            coupons.setRules("50-10");
            coupons.setUserid(currentId);
        } else {
            return RespData.error("兑换码不正确");
        }
        couponsService.save(coupons);
        return RespData.success("兑换成功");
    }

    @GetMapping
    public RespData<List<Coupons>> getCoupons() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Coupons> lam = new LambdaQueryWrapper<>();
        lam.eq(Coupons::getUserid, currentId);
        List<Coupons> couponsList = couponsService.list(lam);
        return RespData.success(couponsList);
    }

    @DeleteMapping
    public RespData<String> deleteCoupons(Long id) {
        couponsService.removeById(id);
        return RespData.success("成功");
    }
}
