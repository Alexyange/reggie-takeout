package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Comments;
import com.itheima.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CommentsOrderDto.java
 * @Description TODO
 * @createTime 2022年02月13日 14:34:00
 */
@Data
public class CommentsOrderDto extends Orders {
    private List<Comments> comments;
}
