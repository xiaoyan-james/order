package com.xiaoyan.Message;

import com.xiaoyan.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(StreamClient.class)
@Slf4j
public class StreamReveiver {

//    @StreamListener(StreamClient.INPUT)
//    public void process(Object message){
//        log.info("StreamReveiver:{}",message);
//    }

    /**
     * 接收orderDTO对象
     * @param message
     */
    @StreamListener(StreamClient.INPUT)//监听input消息
//    @SendTo(StreamClient.INPUT2)//给input2发送消息
    public void process(OrderDTO message){
        log.info("StreamReveiver:{}",message);
//        return "received";
    }

//    @StreamListener(value = StreamClient.INPUT2)
//    public void process2(String message){
//        log.info("StreamReveiver-2:{}",message);
//    }
}
