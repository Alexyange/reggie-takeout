package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Rider;
import com.itheima.reggie.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RiderController.java
 * @Description TODO
 * @createTime 2022年02月28日 19:03:00
 */
@RestController
@Slf4j
@RequestMapping("/rideruser")
public class RiderController {

    @Autowired
    private RiderService riderService;

    @PostMapping("/login")
    public RespData<Rider> login(@RequestBody Rider rider, HttpSession session) {
        LambdaQueryWrapper<Rider> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Rider::getPhone, rider.getPhone());
        lambdaQueryWrapper.eq(Rider::getPassword, rider.getPassword());
        Rider rider1 = riderService.getOne(lambdaQueryWrapper);
        if (rider1 != null) {
            session.setAttribute("rider", rider1.getId());
            return RespData.success(rider1);
        }
        return RespData.error("密码错误");
    }

    @GetMapping("/{phone}")
    public RespData<Rider> getUserData(@PathVariable("phone") String phone) {
        LambdaQueryWrapper<Rider> lam = new LambdaQueryWrapper<>();
        lam.eq(Rider::getPhone, phone);
        Rider rider = riderService.getOne(lam);
        return RespData.success(rider);
    }

    @PutMapping("/info")
    public RespData<Rider> info(@RequestBody Rider rider) {
        LambdaQueryWrapper<Rider> lam = new LambdaQueryWrapper<>();
        lam.eq(Rider::getPhone, rider.getPhone());
        riderService.update(rider, lam);
        return RespData.success(rider);
    }

    @GetMapping("/logout")
    public RespData<String> logout(HttpSession session) {
        session.removeAttribute("rider");
        return RespData.success("成功");
    }
}
