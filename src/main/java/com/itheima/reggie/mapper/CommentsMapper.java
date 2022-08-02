package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Comments;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CommentsMapper.java
 * @Description TODO
 * @createTime 2022年02月13日 14:40:00
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
}
