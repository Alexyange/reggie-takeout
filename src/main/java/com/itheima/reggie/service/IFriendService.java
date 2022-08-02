package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Friend;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-16
 */
public interface IFriendService extends IService<Friend> {
    List<Friend> selectFriend(Long id);
}
