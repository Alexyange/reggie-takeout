package com.itheima.reggie.common;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName RespData.java
 * @Description TODO
 * @createTime 2022年01月21日 20:15:00
 */
@Data
public class RespData<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据
    private Map map = new HashMap(); //动态数据

    public static <T> RespData<T> success(T object) {
        RespData<T> r = new RespData<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> RespData<T> error(String msg) {
        RespData r = new RespData();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public static <T> RespData<T> build(Integer code, String msg) {
        RespData r = new RespData();
        r.msg = msg;
        r.code = code;
        return r;
    }

    public RespData<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
