package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName EmployeeMapper.java
 * @Description TODO
 * @createTime 2022年01月21日 20:05:00
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
