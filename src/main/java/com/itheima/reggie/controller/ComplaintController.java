package com.itheima.reggie.controller;


import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Complaint;
import com.itheima.reggie.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-15
 */
@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    @Autowired
    IComplaintService iComplaintService;

    @GetMapping("/list/{id}")
    public RespData selectComplaint(@PathVariable Long id, HttpSession session) {
        List<Complaint> complaints = iComplaintService.selectdefaultAddress(id, Long.valueOf(session.getAttribute("user").toString()));
        return RespData.success(complaints);
    }

    @PutMapping("/send/{id}")
    public RespData sendComplaint(@PathVariable Long id, @RequestBody Complaint complaint, HttpSession session) {
        complaint.setFriendId(id);
        complaint.setUserId(Long.valueOf(session.getAttribute("user").toString()));
        complaint.setTime(LocalDateTime.now());
        iComplaintService.sendComplaint(complaint);
        return RespData.success(1);
    }
}

