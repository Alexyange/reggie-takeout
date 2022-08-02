package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Coupons;
import com.itheima.reggie.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CouponsDTO.java
 * @Description TODO
 * @createTime 2022年02月03日 10:07:00
 */
@Data
public class CouponsDTO extends User {

    //用户优惠券列表
    private List<Coupons> coupons;

    //单用户   共有多少优惠券
    private Integer count;

    public Integer getCount() {
        return coupons.size();
    }
}
