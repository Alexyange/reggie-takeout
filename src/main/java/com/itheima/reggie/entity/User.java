package com.itheima.reggie.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName User.java
 * @Description TODO 用户信息
 * @createTime 2022年01月28日 18:50:00
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //姓名
    private String name;

    //手机号
    private String phone;

    //性别 0 女 1 男
    private String sex;

    //身份证号
    private String idNumber;

    //头像
    private String avatar;

    //状态 0:禁用，1:正常
    private Integer status;

    //瑞吉豆  默认是 0
    private Long integral;

    //会员字段   0代表吃货卡  1代表瑞吉吃货卡 默认是0
    private Integer members;

    //用户邮箱
    private String email;

    private String password;
}
