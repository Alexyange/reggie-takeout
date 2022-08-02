package com.itheima.reggie.common;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName PayCode.java
 * @Description TODO
 * @createTime 2022年02月16日 10:11:00
 */
public class PayCode {
    public static final String APP_ID = "你的APPID";
    public static final String APP_PRIVATE_KEY = "你的秘钥";
    public static final String ALIPAY_PUBLIC_KEY = "你的公钥";
    public static final String SIGN_TYPE = "RSA2";
    public static final String CHARSET = "UTF-8";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    public static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    public static final String FORMAT = "JSON";
    //签名方式
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    public static final String NOTIFY_URL = "http://127.0.0.1/notifyUrl";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    public static final String RETURN_URLVIP = "http://localhost:8080/aliapy/returnVIPUrl";
    public static final String RETURN_URL2 = "http://localhost:8080/aliapy/returnUrl";
    //public static final String RETURN_URL = "http://124.223.75.149:8080/aliapy/returnUrl";
}
