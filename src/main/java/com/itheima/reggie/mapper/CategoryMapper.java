package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CategoryMapper.java
 * @Description TODO
 * @createTime 2022年01月24日 19:32:00
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
