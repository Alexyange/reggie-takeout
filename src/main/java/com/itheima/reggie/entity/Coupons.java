package com.itheima.reggie.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName Integral.java
 * @Description TODO
 * @createTime 2022年02月03日 09:56:00
 */
@Data
public class Coupons implements Serializable {

    //优惠券的ID
    private Long id;

    //优惠券的名称
    private String name;

    //优惠券的规则
    private String rules;

    //优惠券的描述
    private String description;

    //所拥有的的用户
    private Long userid;

    //优惠券的不可用原因
    private String reason;
}
