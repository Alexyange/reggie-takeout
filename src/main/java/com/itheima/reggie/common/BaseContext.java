package com.itheima.reggie.common;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName BaseContext.java
 * @Description TODO  基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @createTime 2022年01月28日 18:13:00
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     *
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     *
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
