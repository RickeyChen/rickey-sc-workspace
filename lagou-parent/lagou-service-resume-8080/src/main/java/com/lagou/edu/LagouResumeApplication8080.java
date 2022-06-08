package com.lagou.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EntityScan("com.lagou.edu.pojo")
//@EnableEurekaClient //开启Eureka Client - Eureka独有
@EnableDiscoveryClient // 开启注册中心客户端 可以注册到Eureka、Nacos...
public class LagouResumeApplication8080 {

    public static void main(String[] args) {
        SpringApplication.run(LagouResumeApplication8080.class, args);
    }

}