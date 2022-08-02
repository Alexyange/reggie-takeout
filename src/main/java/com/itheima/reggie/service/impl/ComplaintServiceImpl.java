package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Complaint;
import com.itheima.reggie.mapper.ComplaintDao;
import com.itheima.reggie.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-15
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintDao, Complaint> implements IComplaintService {
    @Autowired
    ComplaintDao complaintDao;

    public List<Complaint> selectdefaultAddress(Long id, Long user_id) {
        LambdaQueryWrapper<Complaint> lambdaQueryWrapper = new LambdaQueryWrapper<Complaint>();
        lambdaQueryWrapper.eq(Complaint::getFriendId, id).eq(Complaint::getUserId, user_id);
        lambdaQueryWrapper.or().eq(Complaint::getFriendId, user_id).eq(Complaint::getUserId, id);
        lambdaQueryWrapper.orderBy(true, true, Complaint::getTime, Complaint::getUserId);
        return complaintDao.selectList(lambdaQueryWrapper);
    }

    public void sendComplaint(Complaint complaint) {
        complaintDao.insert(complaint);


    }
}
