package com.xiaoyan.controller;

import com.xiaoyan.Message.StreamClient;
import com.xiaoyan.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SendMsgController {

    @Autowired
    private StreamClient streamClient;

//    @GetMapping("/sendMessage")
//    public  void  process(){
//        String message="now:"+new Date();
//        streamClient.output().send(MessageBuilder.withPayload(message).build());
//
//    }

    /**
     * 发送orderDTO
     */
    @GetMapping("/sendMessage")
    public  void  process(){
        OrderDTO dto=new OrderDTO();
        dto.setOrderId("123456");
        streamClient.output().send(MessageBuilder.withPayload(dto).build());

    }
}
