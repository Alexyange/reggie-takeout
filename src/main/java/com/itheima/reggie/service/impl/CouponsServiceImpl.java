package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Coupons;
import com.itheima.reggie.mapper.CouponsMapper;
import com.itheima.reggie.service.CouponsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName IntegralServiceImpl.java
 * @Description TODO
 * @createTime 2022年02月03日 10:00:00
 */
@Service
@Slf4j
public class CouponsServiceImpl extends ServiceImpl<CouponsMapper, Coupons> implements CouponsService {
}
