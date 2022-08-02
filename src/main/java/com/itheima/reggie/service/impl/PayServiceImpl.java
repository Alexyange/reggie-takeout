package com.itheima.reggie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.PayCode;
import com.itheima.reggie.entity.Aliapay;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.AliapayService;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.PayService;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName PayServiceImpl.java
 * @Description TODO
 * @createTime 2022年02月16日 10:10:00
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AliapayService aliapayService;

    @Autowired
    private UserService userService;


    @Override
    public String toPay(String out_trade_nos, String total_amounts, String subjects, String bodys, String returnurl) {
        //实例化客户端,填入所需参数
        AlipayClient alipayClient = new DefaultAlipayClient(PayCode.GATEWAY_URL, PayCode.APP_ID, PayCode.APP_PRIVATE_KEY, PayCode.FORMAT, PayCode.CHARSET, PayCode.ALIPAY_PUBLIC_KEY, PayCode.SIGN_TYPE);
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        //在公共参数中设置回跳和通知地址
        request.setReturnUrl(returnurl);
        request.setNotifyUrl(PayCode.NOTIFY_URL);
        String body;//商品描述，可空
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", out_trade_nos);//商家订单号
        bizContent.put("total_amount", total_amounts);//付款金额，必填
        bizContent.put("subject", subjects);//订单名称，必填
        bizContent.put("product_code", "QUICK_WAP_WAY");
        request.setBizContent(bizContent.toString());
        String from = "";
        try {
            from = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return from;
    }

    @Override
    public Orders returnUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        System.out.println("=================================同步回调=====================================");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        // 获取支付宝GET过来反馈信息
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println(params);//查看参数都有哪些
        boolean signVerified = AlipaySignature.rsaCheckV1(params, PayCode.ALIPAY_PUBLIC_KEY, PayCode.CHARSET, PayCode.SIGN_TYPE); // 调用SDK验证签名
        //验证签名通过
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号=" + out_trade_no);
            System.out.println("支付宝交易号=" + trade_no);
            System.out.println("付款金额=" + total_amount);

            Aliapay aliapay = new Aliapay();

            aliapay.setId(trade_no);
            aliapay.setOrderId(Long.parseLong(out_trade_no));
            aliapay.setAmount(new BigDecimal(total_amount));
            aliapay.setStatus(1);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            aliapay.setOrdertime(LocalDateTime.now());
            //支付成功，修复支付状态
            aliapayService.save(aliapay);
            Orders orders = orderService.getById(out_trade_no);
            return orders;

        }
        return null;
    }

    @Override
    public Aliapay toFefund(long id) {
        LambdaQueryWrapper<Aliapay> lam = new LambdaQueryWrapper<>();
        lam.eq(Aliapay::getOrderId, id);
        Aliapay aliapay = aliapayService.getOne(lam);
        //实例化客户端,填入所需参数
        AlipayClient alipayClient = new DefaultAlipayClient(PayCode.GATEWAY_URL, PayCode.APP_ID, PayCode.APP_PRIVATE_KEY, PayCode.FORMAT, PayCode.CHARSET, PayCode.ALIPAY_PUBLIC_KEY, PayCode.SIGN_TYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        String trade_no = aliapay.getId();
        //付款金额，必填
        String refund_amount = aliapay.getAmount().toString();
        //订单名称，必填
        String out_request_no = aliapay.getOrderId().toString();
        aliapay.setStatus(2);
        //商品描述，可空
        Long currentId = BaseContext.getCurrentId();
        User user = userService.getById(currentId);
        user.setIntegral(user.getIntegral() - Long.parseLong(refund_amount));
        userService.updateById(user);
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", trade_no);
        bizContent.put("refund_amount", refund_amount);
        bizContent.put("out_request_no", out_request_no);

        request.setBizContent(bizContent.toString());
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            System.out.println();
            aliapay.setOrdertime(LocalDateTime.now());
            if (response.isSuccess()) {
                Orders orders = new Orders();
                orders.setId(id);
                orders.setStatus(5);
                boolean updateById = orderService.updateById(orders);
                aliapayService.updateById(aliapay);
                return aliapay;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }


}
