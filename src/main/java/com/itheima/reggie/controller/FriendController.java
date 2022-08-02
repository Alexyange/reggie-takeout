package com.itheima.reggie.controller;


import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Friend;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.IFriendService;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-16
 */
@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    IFriendService iFriendService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public RespData selectComplaint(HttpSession session) {
        List<Friend> friends = iFriendService.selectFriend(Long.valueOf(session.getAttribute("user").toString()));
        return RespData.success(friends);
    }

    @GetMapping("/add")
    public RespData selectComplaint(Long id, HttpSession session) {
        Long user = Long.valueOf(session.getAttribute("user").toString());
        User byId = userService.getById(id);
        if (byId == null) return RespData.error("没有该用户");
        String name = byId.getName();
        Friend friend = new Friend();
        friend.setFriendName(name);
        friend.setId(user);
        friend.setFriendId(id);
        iFriendService.save(friend);
        User byId1 = userService.getById(user);
        friend.setFriendName(byId1.getName());
        friend.setId(id);
        friend.setFriendId(user);
        iFriendService.save(friend);
        return RespData.success(1);
    }
}

