package com.xiaoyan.Message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xiaoyan.product.common.ProductInfoOutput;
import com.xiaoyan.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductInfoReceiver {

    private static  final String PRODUCT_STOCK_TEMPLATE="product_stock_%s";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String message){
        //message =>ProductInfoOutput
        List<ProductInfoOutput> outputList = JsonUtil.toObject(message,
                new TypeReference<List<ProductInfoOutput>>() {
        });
        //product服务扣完库存后，发送消息，order服务接收
        log.info("接收消息队列{}的消息：{}","productInfo",outputList);

        //存储到redis中
        for(ProductInfoOutput output:outputList){
            stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE, output.getProductId()),
                    String.valueOf(output.getProductStock()));
        }
    }
}
