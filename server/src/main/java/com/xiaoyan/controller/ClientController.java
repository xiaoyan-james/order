package com.xiaoyan.controller;

import com.xiaoyan.product.client.ProductClient;
import com.xiaoyan.product.common.DecreaseStockInput;
import com.xiaoyan.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RefreshScope //用来刷新git上的配置文件更新
public class ClientController {
    @Value("${env}")
    private String env;

    @Autowired
    private ProductClient productClient;

    @GetMapping("/getProductMsg")
    public String getProductMsg(){
        RestTemplate template=new RestTemplate();
        //第一种方式redisTemplate
//        String responst=template.getForObject("http://localhost:8762/msg",String.class);
//        log.info("response={}",responst);

        //第二种方式 LoadBalancerClient
//        ServiceInstance serviceInstance=loadBalancerClient.choose("product");
//        String url=String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort())+"/msg";
//        String responst=template.getForObject(url,String.class);
//        log.info("response={}",responst);

        //第三种 利用注解@LoadBalanced ，可在restTemplate里面用应用名字
//        String responst=restTemplate.getForObject("http://product/msg",String.class);
//        log.info("response={}",responst);

//        String responst=productClient.productMsg();
        String responst=env+"----"+env;
        log.info("response={}",responst);
        return  responst;

    }

    @GetMapping("/getListProduct")
    public String getListProduct(){
        List<ProductInfoOutput> list =productClient.listForOrder(Arrays.asList("164103465734242707"));
        log.info("response={}",list);
        return "success";
    }

    @GetMapping("/decreaseStock")
    public String decrease(){
        productClient.decreaseStock(Arrays.asList(new DecreaseStockInput("164103465734242707",1)));
        return "扣库存成功";
    }
}
