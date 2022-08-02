package com.itheima.reggie.dto;

import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrdersDto.java
 * @Description TODO
 * @createTime 2022年01月29日 15:35:00
 */
@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
}
