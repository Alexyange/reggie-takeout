package com.itheima.reggie.config;

import com.itheima.reggie.controller.WebSocketTest;
import com.itheima.reggie.service.IComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @描述 开启WebSocket支持的配置类
 * @创建人 haoqian
 * @创建时间 2021/5/20
 */
@Configuration
@Service
public class WebSocket {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setIComplaintService(IComplaintService iComplaintService) {
        WebSocketTest.iComplaintService = iComplaintService;
    }

}
