package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName SetmealDto.java
 * @Description TODO
 * @createTime 2022年01月28日 17:01:00
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;//套餐关联的菜品集合

    private String categoryName;//分类名称
}
