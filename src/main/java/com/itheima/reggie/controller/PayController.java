package com.itheima.reggie.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.PayCode;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Aliapay;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.AliapayService;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.PayService;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName PayController.java
 * @Description TODO
 * @createTime 2022年02月15日 09:12:00
 */
@Controller
@Slf4j
@RequestMapping("/aliapy")
@ResponseBody
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AliapayService aliapayService;

    @Autowired
    private UserService userService;

    @GetMapping("/pay/{id}")
    public void alipay(HttpServletResponse httpResponse, @PathVariable Long id) throws IOException {
        Orders orders = orderService.getById(id);
        String out_trade_nos = orders.getId().toString();
        String total_amounts = orders.getAmount().toString();
        String form = payService.toPay(out_trade_nos, total_amounts, "瑞吉外卖商品总额", "", PayCode.RETURN_URL2);
        orders.setStatus(2);
        orderService.updateById(orders);
        httpResponse.setContentType("text/html;charset=" + PayCode.CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @GetMapping("/vippay")
    public void alipayVip(HttpServletResponse httpResponse) throws IOException {
        String out_trade_nos = IdWorker.getIdStr();
        Long amount = 15l;
        String total_amounts = amount.toString();
        String form = payService.toPay(out_trade_nos, total_amounts, "瑞吉吃货卡购买", "", PayCode.RETURN_URLVIP);
        httpResponse.setContentType("text/html;charset=" + PayCode.CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @GetMapping("/fefund/{id}")
    public RespData<Aliapay> toFefund(@PathVariable Long id) throws IOException {
        Orders orders = orderService.getById(id);
        if (orders.getStatus() != 1) {
            Aliapay aliapay = payService.toFefund(id);
            if (null != aliapay) {
                return RespData.success(aliapay);
            }
        } else {
            orders.setStatus(5);
            orderService.updateById(orders);
            return RespData.build(2, "取消成功");
        }
        return RespData.error("出错了！");
    }

    @GetMapping("/returnUrl")
    public void returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        Orders order = payService.returnUrl(request, response);
        if (null != order) {
            response.sendRedirect("http://localhost:8080/front/page/pay-success.html?id=" + order.getAddressBookId() + "=" + order.getId());//跳转付款成功页面
            //response.sendRedirect("http://124.223.75.149:8080/front/page/pay-success.html");//跳转付款成功页面
        }
    }

    @GetMapping("/returnVIPUrl")
    public void returnVIPUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        Orders order = payService.returnUrl(request, response);
        Long currentId = BaseContext.getCurrentId();
        User user = userService.getById(currentId);
        user.setMembers(1);
        userService.updateById(user);
        response.sendRedirect("http://localhost:8080/front/page/members.html");//跳转付款成功页面
        //response.sendRedirect("http://124.223.75.149:8080/front/page/members.html");//跳转付款成功页面
    }

}
