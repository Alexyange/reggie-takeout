package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Friend;
import com.itheima.reggie.mapper.FriendDao;
import com.itheima.reggie.service.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-16
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendDao, Friend> implements IFriendService {
    @Autowired
    FriendDao friendDao;

    public List<Friend> selectFriend(Long id) {
        LambdaQueryWrapper<Friend> lambdaQueryWrapper = new LambdaQueryWrapper<Friend>();
        lambdaQueryWrapper.eq(Friend::getId, id);
        List<Friend> friends = friendDao.selectList(lambdaQueryWrapper);
        return friends;

    }
}
