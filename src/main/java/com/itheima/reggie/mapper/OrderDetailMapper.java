package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName OrderDetailMapper.java
 * @Description TODO
 * @createTime 2022年01月28日 21:22:00
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    @Select({"<script>", "SELECT sum(number) as num,name,dish_id FROM",
            "(select name,dish_id,number FROM order_detail where order_id in (",
            "SELECT id FROM orders ",
            "<if test='beginTime!=null and endTime!=null'>", "where order_time BETWEEN #{beginTime} and #{endTime}", "</if>",
            ")) as a",
            "GROUP BY name",
            "</script>"})
    List<Map<String, Object>> findByDateAndDishId(Date beginTime, Date endTime);

    @Select({"<script>", "SELECT sum(number) as num,name,setmeal_id FROM",
            "(select name,number,setmeal_id FROM order_detail where order_id in (",
            "SELECT id FROM orders",
            "<if test='beginTime!=null and endTime!=null'>", "where order_time BETWEEN #{beginTime} and #{endTime}", "</if>",
            ")) as a",
            "GROUP BY name",
            "</script>"})
    List<Map<String, Object>> findByDateAndSetmealId(Date beginTime, Date endTime);

    @Select({"<script>", "select amount,number FROM order_detail where order_id in (",
            "SELECT id FROM orders",
            "<if test='beginTime!=null and endTime!=null'>",
            "where order_time BETWEEN #{beginTime} and #{endTime}", "</if>",
            ") AND setmeal_id IS NULL", "</script>"})
    List<Map<String, Object>> getDishAmount(Date beginTime, Date endTime);

    @Select({"<script>", "select amount,number FROM order_detail where order_id in (",
            "SELECT id FROM orders",
            "<if test='beginTime!=null and endTime!=null'>",
            "where order_time BETWEEN #{beginTime} and #{endTime}", "</if>",
            ") AND setmeal_id IS NOT NULL", "</script>"})
    List<Map<String, Object>> getSetmealAmount(Date beginTime, Date endTime);
}
