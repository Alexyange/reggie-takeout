package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Complaint;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-15
 */
@Mapper
public interface ComplaintDao extends BaseMapper<Complaint> {

}
