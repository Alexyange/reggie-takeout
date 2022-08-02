package com.itheima.reggie.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RiderOrder.java
 * @Description TODO
 * @createTime 2022年02月28日 19:50:00
 */
@Data
public class RiderOrder implements Serializable {

    private Long id;

    private Long riderId;

    private LocalDateTime issueTime;

    private LocalDateTime preemptTime;

    private LocalDateTime accomplishTime;

    private String address1;

    private String address2;

    private Integer tips;

    private Long orderId;

    private String phone;

    private String xpoint;

    private String ypoint;

    private Integer status;

    private String distance;

    private String finishtime;
}
