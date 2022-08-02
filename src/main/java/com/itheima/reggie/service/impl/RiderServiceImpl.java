package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Rider;
import com.itheima.reggie.mapper.RiderMapper;
import com.itheima.reggie.service.RiderService;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RiderServiceImpl.java
 * @Description TODO
 * @createTime 2022年02月28日 19:02:00
 */
@Service
public class RiderServiceImpl extends ServiceImpl<RiderMapper, Rider> implements RiderService {
}
