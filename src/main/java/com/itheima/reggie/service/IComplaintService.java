package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Complaint;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-15
 */
public interface IComplaintService extends IService<Complaint> {

    List<Complaint> selectdefaultAddress(Long id, Long user_id);

    void sendComplaint(Complaint complaint);
}
