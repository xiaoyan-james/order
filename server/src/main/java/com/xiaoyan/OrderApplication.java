package com.xiaoyan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
//@EnableEurekaClient
//@EnableCircuitBreaker
//上面三个注解 = @SpringCloudApplication
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.xiaoyan.product.client")
@ComponentScan(basePackages = "com.xiaoyan")
@EnableHystrixDashboard
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}


