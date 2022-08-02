package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName DishDto.java
 * @Description TODO  再Dish的基础上  封装一个List集合  内容是DishFlavor实体类，再加入categoryName名称
 * @createTime 2022年01月28日 15:02:00
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
