package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EntityScan("com.lagou.edu.pojo")
//@EnableEurekaClient //开启Eureka Client - Eureka独有
@EnableDiscoveryClient // 开启注册中心客户端 可以注册到Eureka、Nacos...
public class LagouResumeApplication8081 {

    public static void main(String[] args) {
        SpringApplication.run(LagouResumeApplication8081.class, args);
    }

}
