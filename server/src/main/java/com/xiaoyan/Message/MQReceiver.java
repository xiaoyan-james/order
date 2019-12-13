package com.xiaoyan.Message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接受mq消息
 */
@Slf4j
@Component
public class MQReceiver {

//    @RabbitListener(queues = "myQueue")
//    自动创建队列
//    @RabbitListener(queuesToDeclare = @Queue("myQu"))
//    自动创建exchange和queue
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue("myQueue"),
//            exchange = @Exchange("myExchange")
//    ))
//    public void  process(String message){
//        log.info("MQreceiver:{}",message);
//    }

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myExchange"),
            key = "computer",
            value = @Queue("computerOrder")
    ))
    public void  orderReceiver(String message){
        log.info("computer----MQreceiver:{}",message);
    }

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myExchange"),
            key = "fruit",
            value = @Queue("fruitOrder")
    ))
    public void  orderReceiver2(String message){
        log.info("fruit----MQreceiver:{}",message);
    }
}
