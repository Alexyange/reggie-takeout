package com.itheima.reggie.service;

import com.alipay.api.AlipayApiException;
import com.itheima.reggie.entity.Aliapay;
import com.itheima.reggie.entity.Orders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName PayService.java
 * @Description TODO
 * @createTime 2022年02月16日 10:09:00
 */
public interface PayService {

    String toPay(String out_trade_nos, String total_amounts, String subjects, String bodys, String returnurl);

    Orders returnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException;

    Aliapay toFefund(long id);
}
