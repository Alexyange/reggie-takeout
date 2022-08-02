package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Comments;
import com.itheima.reggie.service.CommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName CommentsController.java
 * @Description TODO
 * @createTime 2022年02月13日 15:04:00
 */
@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping("/add")
    public RespData<String> addComments(@RequestBody Comments comments) {
        comments.setId(IdWorker.getId());
        comments.setCommtime(LocalDateTime.now());
        commentsService.save(comments);
        return RespData.success("添加成功");
    }

    @PostMapping("/get")
    public RespData<List<Comments>> getComments(@RequestBody Comments comments) {
        LambdaQueryWrapper<Comments> lam = new LambdaQueryWrapper<>();
        lam.eq(Comments::getOrderId, comments.getOrderId());
        List<Comments> commentsList = commentsService.list(lam);
        return RespData.success(commentsList);
    }

    @PostMapping("/num")
    public RespData<Long> getCommentsNum(@RequestBody Comments comments) {
        QueryWrapper qw = new QueryWrapper<>();
        qw.select("count(*) as num");
        qw.eq("order_id", comments.getOrderId());
        Map map = commentsService.getMap(qw);
        Long num = (Long) map.get("num");
        return RespData.success(num);
    }
}
