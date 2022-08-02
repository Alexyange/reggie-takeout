package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Comments;
import com.itheima.reggie.mapper.CommentsMapper;
import com.itheima.reggie.service.CommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CommentsServiceImpl.java
 * @Description TODO
 * @createTime 2022年02月13日 14:41:00
 */
@Service
@Slf4j
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {
}
