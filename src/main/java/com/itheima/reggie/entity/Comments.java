package com.itheima.reggie.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName Comments.java
 * @Description TODO
 * @createTime 2022年02月13日 11:43:00
 */
@Data
public class Comments implements Serializable {

    private Long id;

    private String content;

    private Integer commentsnum;

    private Long orderId;

    private LocalDateTime commtime;
}
