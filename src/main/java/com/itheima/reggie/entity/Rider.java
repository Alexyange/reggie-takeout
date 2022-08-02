package com.itheima.reggie.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName Rider.java
 * @Description TODO
 * @createTime 2022年02月28日 18:57:00
 */
@Data
public class Rider implements Serializable {

    private Long id;

    private String name;

    private Long userCid;

    private String tel;

    private Double balance;

    private String password;

    private String imag;

    private Integer credit;

    private String phone;

    private String pic;

}
