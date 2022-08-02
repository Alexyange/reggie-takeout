package com.itheima.reggie.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName Aliapay.java
 * @Description TODO
 * @createTime 2022年02月15日 14:16:00
 */
@Data
public class Aliapay implements Serializable {
    private String id;

    private Long orderId;

    private LocalDateTime ordertime;

    private BigDecimal amount;

    private Integer status;
}
