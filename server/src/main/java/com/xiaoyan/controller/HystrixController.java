package com.xiaoyan.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @author: yanguojun
 * @Date: 2019/12/2 17:13
 * @Description:
 */
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
@Slf4j
public class HystrixController {


    //    @HystrixCommand(fallbackMethod = "fallback")
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),//超时配置
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true" ),//设置熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10" ),//请求量阈值
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000" ),//
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60" ),
    })
//    @HystrixCommand
    @GetMapping("/getProductInfoList")
    public String getProductInfoList(@RequestParam ("number") Integer number) {
        if(number%2==0){
            return "number=2,success!";
        }
        RestTemplate template = new RestTemplate();
        return template.postForObject("http://127.0.0.1:8763/product/listForOrder",
                Arrays.asList("157875196366160022"), String.class);
//        throw  new RuntimeException("有异常了----");
    }

    private String fallback() {
        return "太拥挤了，请稍后再试~~";
    }

    private String defaultFallback() {
        log.info("服务降级--调用的方法fallback");
        return "默认提示--太拥挤了，请稍后再试~~";
    }
}
