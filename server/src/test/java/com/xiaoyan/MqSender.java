package com.xiaoyan;


import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 发送mq消息测试
 */
@Component
public class MqSender extends  OrderApplicationTests {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public  void  send(){
        amqpTemplate.convertAndSend("myQueue","nowTime:"+new Date());

    }

    @Test
    public  void  sendComputer(){
        amqpTemplate.convertAndSend("myExchange","computer","nowTime:"+new Date());
    }

    @Test
    public  void  sendFruit(){
        amqpTemplate.convertAndSend("myExchange","fruit","nowTime:"+new Date());
    }
}
